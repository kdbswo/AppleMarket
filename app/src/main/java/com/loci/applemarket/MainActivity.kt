package com.loci.applemarket

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loci.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var detailProductData: Product

    val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 300 }
    val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 300 }
    var isTop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, btnBackCallback)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ProductData.allListAdd()

        binding.floatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ABABAB")))

        val adapter = MyAdapter(ProductData.productList)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)

        val decoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.mainRecyclerView.addItemDecoration(decoration)

        binding.ivMainNotification.setOnClickListener {
            setNotificationPermission()
            notification()
        }

        binding.mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                setFloatingButton(newState)
            }
        })

        binding.floatingButton.setOnClickListener {
            binding.mainRecyclerView.smoothScrollToPosition(0)
        }

        setResultProduct()

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                detailProductData = ProductData.productList[position]
                intent.putExtra("product", detailProductData)
                intent.putExtra("position", position)

                resultLauncher.launch(intent)

            }

            override fun onLongClick(view: View, position: Int) {
                deleteDialog(position)
            }

        }

    }

    private val btnBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            MyDialogFragment().show(
                supportFragmentManager,
                "MyDialogFragment"
            )
        }
    }

    private fun setFloatingButton(newState: Int) {
        if (!binding.mainRecyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            binding.floatingButton.startAnimation(fadeOut)
            binding.floatingButton.visibility = View.GONE
            isTop = true
        } else {
            if (isTop) {
                binding.floatingButton.visibility = View.VISIBLE
                binding.floatingButton.startAnimation(fadeIn)
                isTop = false
            }
        }
    }

    private fun setResultProduct() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val productData = result.data?.getParcelableExtra<Product>("product")!!
                    val productPosition = result.data?.getIntExtra("position", -1)

                    binding.mainRecyclerView.adapter?.notifyItemChanged(productPosition!!)

                }
            }
    }

    private fun setNotificationPermission() {
        //api 33이상일 경우 알림 허가 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }

    private fun deleteDialog(position: Int) {
        val dialog: AlertDialog = this@MainActivity.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            builder.apply {
                this.setIcon(R.drawable.chat)
                this.setMessage("상품을 정말 삭제하시겠습니까?")
                this.setTitle("상품 삭제")
                this.setPositiveButton("확인") { dialog, _ ->
                    ProductData.removeProduct(position)
                    binding.mainRecyclerView.adapter?.notifyDataSetChanged()

                    dialog.dismiss()
                }
                this.setNegativeButton("취소") { dialog, _ ->
                    dialog.cancel()

                }
            }
            builder.create()
        }
        dialog.show()
    }

    private fun notification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 26버전 이상 일경우 채널 생성
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channelId)

        } else {
            builder = NotificationCompat.Builder(this)
        }

//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
        builder.run {
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림").setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            addAction(R.mipmap.ic_launcher, "Action", pendingIntent)
        }

        manager.notify(11, builder.build())

    }

}













