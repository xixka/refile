package com.webdavrenamer.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TmdbCacheDao_Impl implements TmdbCacheDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TmdbCacheEntity> __insertionAdapterOfTmdbCacheEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public TmdbCacheDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTmdbCacheEntity = new EntityInsertionAdapter<TmdbCacheEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tmdb_cache` (`id`,`cacheKey`,`mediaType`,`tmdbId`,`language`,`seasonNumber`,`responseJson`,`cachedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TmdbCacheEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCacheKey() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCacheKey());
        }
        if (entity.getMediaType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMediaType());
        }
        statement.bindLong(4, entity.getTmdbId());
        if (entity.getLanguage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLanguage());
        }
        if (entity.getSeasonNumber() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSeasonNumber());
        }
        if (entity.getResponseJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getResponseJson());
        }
        statement.bindLong(8, entity.getCachedAt());
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tmdb_cache WHERE cachedAt < ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tmdb_cache";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TmdbCacheEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTmdbCacheEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByKey(final String key,
      final Continuation<? super TmdbCacheEntity> $completion) {
    final String _sql = "SELECT * FROM tmdb_cache WHERE cacheKey = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TmdbCacheEntity>() {
      @Override
      @Nullable
      public TmdbCacheEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCacheKey = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheKey");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaType");
          final int _cursorIndexOfTmdbId = CursorUtil.getColumnIndexOrThrow(_cursor, "tmdbId");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfSeasonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonNumber");
          final int _cursorIndexOfResponseJson = CursorUtil.getColumnIndexOrThrow(_cursor, "responseJson");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final TmdbCacheEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCacheKey;
            if (_cursor.isNull(_cursorIndexOfCacheKey)) {
              _tmpCacheKey = null;
            } else {
              _tmpCacheKey = _cursor.getString(_cursorIndexOfCacheKey);
            }
            final String _tmpMediaType;
            if (_cursor.isNull(_cursorIndexOfMediaType)) {
              _tmpMediaType = null;
            } else {
              _tmpMediaType = _cursor.getString(_cursorIndexOfMediaType);
            }
            final int _tmpTmdbId;
            _tmpTmdbId = _cursor.getInt(_cursorIndexOfTmdbId);
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final Integer _tmpSeasonNumber;
            if (_cursor.isNull(_cursorIndexOfSeasonNumber)) {
              _tmpSeasonNumber = null;
            } else {
              _tmpSeasonNumber = _cursor.getInt(_cursorIndexOfSeasonNumber);
            }
            final String _tmpResponseJson;
            if (_cursor.isNull(_cursorIndexOfResponseJson)) {
              _tmpResponseJson = null;
            } else {
              _tmpResponseJson = _cursor.getString(_cursorIndexOfResponseJson);
            }
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _result = new TmdbCacheEntity(_tmpId,_tmpCacheKey,_tmpMediaType,_tmpTmdbId,_tmpLanguage,_tmpSeasonNumber,_tmpResponseJson,_tmpCachedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
