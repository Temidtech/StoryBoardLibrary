package com.temidayoadefioye.storyboardlibrary

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide

import com.temidayoadefioye.storyboard.StoryBoardProgressView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StoryBoardProgressView.StoriesListener {
    private var storyBoardProgressView: StoryBoardProgressView? = null

    private var counter = 0
    private val resources = intArrayOf(R.drawable.sample1, R.drawable.sample2, R.drawable.sample3, R.drawable.sample4)

    private val durations = longArrayOf(500L, 1000L, 1500L, 4000L)

    internal var pressTime = 0L
    internal var limit = 500L

    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storyBoardProgressView!!.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storyBoardProgressView!!.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        Glide
                .with(this)
                .load("https://pbs.twimg.com/profile_images/699217734492647428/pCfEzr6L_400x400.png")
                .transform(CircleTransform(this))
                .placeholder(R.drawable.ic_arrow_back_black_24dp)
                .into(imgProfile)
        storyBoardProgressView = findViewById<View>(R.id.stories) as StoryBoardProgressView
        storyBoardProgressView!!.setStoriesCount(PROGRESS_COUNT)
        storyBoardProgressView!!.setStoryDuration(3000L)
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storyBoardProgressView!!.setStoriesListener(this)
        storyBoardProgressView!!.startStories()

        image!!.setImageResource(resources[counter])

        // bind reverse view
        val reverse = findViewById<View>(R.id.reverse)
        reverse.setOnClickListener { storyBoardProgressView!!.reverse() }
        reverse.setOnTouchListener(onTouchListener)

        // bind skip view
        val skip = findViewById<View>(R.id.skip)
        skip.setOnClickListener { storyBoardProgressView!!.skip() }
        skip.setOnTouchListener(onTouchListener)

    }

    public override fun onStop() {
        super.onStop()
    }

    override fun onNext() {
        image!!.setImageResource(resources[++counter])
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        image!!.setImageResource(resources[--counter])
    }

    override fun onComplete() {}
    public override fun onDestroy() {
        // Very important !
        storyBoardProgressView!!.destroy()
        super.onDestroy()
    }

    companion object {
        private val PROGRESS_COUNT =4
    }
}
