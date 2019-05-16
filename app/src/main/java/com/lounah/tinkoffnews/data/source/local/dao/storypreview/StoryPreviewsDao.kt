package com.lounah.tinkoffnews.data.source.local.dao.storypreview

import android.database.sqlite.SQLiteDatabase
import com.lounah.tinkoffnews.data.source.local.entity.StoryPreviewEntity
import com.lounah.tinkoffnews.data.source.local.sql.queryengine.InsertQueryEngine
import com.lounah.tinkoffnews.data.source.local.sql.queryengine.SelectQueryEngine
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import com.lounah.tinkoffnews.data.source.local.sql.DatabaseContract
import android.content.ContentValues
import com.lounah.tinkoffnews.data.source.local.dao.BaseDao
import java.util.*


class StoryPreviewsDao @Inject constructor(
    private val sqLiteDatabase: SQLiteDatabase,
    private val selectQueryEngine: SelectQueryEngine<StoryPreviewEntity>,
    private val insertQueryEngine: InsertQueryEngine
): BaseDao<StoryPreviewEntity> {

    private companion object {
        private val SELECT_QUERY = "SELECT * FROM ${DatabaseContract.StoryPreviewEntityTable.TABLE_NAME}"
    }

    override fun getAll(): Single<List<StoryPreviewEntity>> {
        return Single.create { emitter ->
            val result = selectQueryEngine.executeRawQuery(SELECT_QUERY, sqLiteDatabase, DatabaseContract.StoryPreviewEntityTable.TABLE_NAME,  0)
            emitter.onSuccess(result.sortedBy { Date(it.date) }.reversed())
        }
    }

    fun getAll(limit: Int, offset: Int): Single<List<StoryPreviewEntity>> {
        val SELECT_QUERY = "SELECT * FROM ${DatabaseContract.StoryPreviewEntityTable.TABLE_NAME} ORDER BY ${DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_DATE} DESC limit $limit offset $offset"
        return Single.create { emitter ->
            val result = selectQueryEngine.executeRawQuery(SELECT_QUERY, sqLiteDatabase, DatabaseContract.StoryPreviewEntityTable.TABLE_NAME,  0)
            emitter.onSuccess(result.sortedBy { Date(it.date) }.reversed())
        }
    }

    override fun add(item: StoryPreviewEntity): Completable {
        val contentValues = ContentValues().apply {
            put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_ID, item.id)
            put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_NAME, item.name)
            put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_TEXT, item.text)
            put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_IS_BOOKMARKED, item.isBookmarked)
            put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_DATE, item.date)
        }

        return Completable.fromAction {
            insertQueryEngine.execute(
                    "",
                    sqLiteDatabase,
                    DatabaseContract.StoryPreviewEntityTable.TABLE_NAME,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE)
        }
    }

    override fun addAll(items: List<StoryPreviewEntity>): Completable {
        return Completable.fromAction {
            items.forEach { item ->
                val contentValues = ContentValues().apply {
                    put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_ID, item.id)
                    put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_NAME, item.name)
                    put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_TEXT, item.text)
                    put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_IS_BOOKMARKED, item.isBookmarked)
                    put(DatabaseContract.StoryPreviewEntityTable.COLUMN_NAME_DATE, item.date)
                }

                insertQueryEngine.execute(
                        "",
                        sqLiteDatabase,
                        DatabaseContract.StoryPreviewEntityTable.TABLE_NAME,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE)

            }
        }
    }
}