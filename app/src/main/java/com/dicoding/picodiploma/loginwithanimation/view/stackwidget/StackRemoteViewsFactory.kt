package com.dicoding.picodiploma.loginwithanimation.view.stackwidget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserRepository2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<ListStoryItem>()

    private lateinit var userPreference: UserPreference
    private lateinit var apiService: ApiService
    private lateinit var repository2: UserRepository2

    override fun onCreate() {
        userPreference = userPreference
        apiService = apiService

        // Fetch list of stories when the widget is created
        fetchData()
    }

    override fun onDataSetChanged() {
        // This method is triggered when notifyDataSetChanged() is called
        fetchData()
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Get token from UserPreference or use a default token if available
                val token = userPreference.getSession().toString()

                // Call UserRepository2 to get the list of stories
                val repository = UserRepository2.getInstance(userPreference, apiService)
                val stories = repository.getListStory(token)

                // Update widget items with the fetched stories
                mWidgetItems.clear()
            } catch (e: Exception) {
                // Handle exceptions if needed
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val item = mWidgetItems[position]

        // Set the story title or content to the widget
        rv.setTextViewText(R.id.banner_text, item.description)

        // Load image using Glide or your preferred image loading library
        Glide.with(mContext)
            .load(item.photoUrl)


        val extras = Intent().apply {
            putExtra(ImagesBannerWidget.EXTRA_ITEM, position)
        }
        val fillInIntent = Intent().apply {
            putExtras(extras)
        }

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = i.toLong()

    override fun hasStableIds(): Boolean = true
}
