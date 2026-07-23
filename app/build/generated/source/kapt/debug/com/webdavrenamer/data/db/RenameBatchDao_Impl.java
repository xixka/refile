package com.webdavrenamer.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RenameBatchDao_Impl implements RenameBatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RenameBatchEntity> __insertionAdapterOfRenameBatchEntity;

  private final EntityInsertionAdapter<RenameEntryEntity> __insertionAdapterOfRenameEntryEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkReverted;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBatch;

  public RenameBatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRenameBatchEntity = new EntityInsertionAdapter<RenameBatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rename_batches` (`id`,`serverId`,`serverName`,`batchName`,`createdAt`,`totalOperations`,`succeededCount`,`failedCount`,`isReverted`,`revertedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RenameBatchEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getServerId());
        if (entity.getServerName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getServerName());
        }
        if (entity.getBatchName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBatchName());
        }
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getTotalOperations());
        statement.bindLong(7, entity.getSucceededCount());
        statement.bindLong(8, entity.getFailedCount());
        final int _tmp = entity.isReverted() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getRevertedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getRevertedAt());
        }
      }
    };
    this.__insertionAdapterOfRenameEntryEntity = new EntityInsertionAdapter<RenameEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rename_entries` (`id`,`batchId`,`sourcePath`,`targetPath`,`mediaType`,`companionsJson`,`status`,`errorMessage`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RenameEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBatchId());
        if (entity.getSourcePath() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSourcePath());
        }
        if (entity.getTargetPath() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTargetPath());
        }
        if (entity.getMediaType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMediaType());
        }
        if (entity.getCompanionsJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCompanionsJson());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getStatus());
        }
        if (entity.getErrorMessage() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getErrorMessage());
        }
      }
    };
    this.__preparedStmtOfMarkReverted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE rename_batches SET isReverted = 1, revertedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBatch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rename_batches WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBatch(final RenameBatchEntity batch,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRenameBatchEntity.insertAndReturnId(batch);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEntries(final List<RenameEntryEntity> entries,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRenameEntryEntity.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBatchWithEntries(final RenameBatchEntity batch,
      final List<RenameEntryEntity> entries, final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RenameBatchDao.DefaultImpls.insertBatchWithEntries(RenameBatchDao_Impl.this, batch, entries, __cont), $completion);
  }

  @Override
  public Object markReverted(final long id, final long revertedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkReverted.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, revertedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfMarkReverted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBatch(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBatch.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteBatch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RenameBatchEntity>> observeBatches() {
    final String _sql = "SELECT * FROM rename_batches ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rename_batches"}, new Callable<List<RenameBatchEntity>>() {
      @Override
      @NonNull
      public List<RenameBatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfServerName = CursorUtil.getColumnIndexOrThrow(_cursor, "serverName");
          final int _cursorIndexOfBatchName = CursorUtil.getColumnIndexOrThrow(_cursor, "batchName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfTotalOperations = CursorUtil.getColumnIndexOrThrow(_cursor, "totalOperations");
          final int _cursorIndexOfSucceededCount = CursorUtil.getColumnIndexOrThrow(_cursor, "succeededCount");
          final int _cursorIndexOfFailedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failedCount");
          final int _cursorIndexOfIsReverted = CursorUtil.getColumnIndexOrThrow(_cursor, "isReverted");
          final int _cursorIndexOfRevertedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "revertedAt");
          final List<RenameBatchEntity> _result = new ArrayList<RenameBatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RenameBatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpServerId;
            _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            final String _tmpServerName;
            if (_cursor.isNull(_cursorIndexOfServerName)) {
              _tmpServerName = null;
            } else {
              _tmpServerName = _cursor.getString(_cursorIndexOfServerName);
            }
            final String _tmpBatchName;
            if (_cursor.isNull(_cursorIndexOfBatchName)) {
              _tmpBatchName = null;
            } else {
              _tmpBatchName = _cursor.getString(_cursorIndexOfBatchName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmpTotalOperations;
            _tmpTotalOperations = _cursor.getInt(_cursorIndexOfTotalOperations);
            final int _tmpSucceededCount;
            _tmpSucceededCount = _cursor.getInt(_cursorIndexOfSucceededCount);
            final int _tmpFailedCount;
            _tmpFailedCount = _cursor.getInt(_cursorIndexOfFailedCount);
            final boolean _tmpIsReverted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsReverted);
            _tmpIsReverted = _tmp != 0;
            final Long _tmpRevertedAt;
            if (_cursor.isNull(_cursorIndexOfRevertedAt)) {
              _tmpRevertedAt = null;
            } else {
              _tmpRevertedAt = _cursor.getLong(_cursorIndexOfRevertedAt);
            }
            _item = new RenameBatchEntity(_tmpId,_tmpServerId,_tmpServerName,_tmpBatchName,_tmpCreatedAt,_tmpTotalOperations,_tmpSucceededCount,_tmpFailedCount,_tmpIsReverted,_tmpRevertedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBatch(final long id, final Continuation<? super RenameBatchEntity> $completion) {
    final String _sql = "SELECT * FROM rename_batches WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RenameBatchEntity>() {
      @Override
      @Nullable
      public RenameBatchEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfServerName = CursorUtil.getColumnIndexOrThrow(_cursor, "serverName");
          final int _cursorIndexOfBatchName = CursorUtil.getColumnIndexOrThrow(_cursor, "batchName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfTotalOperations = CursorUtil.getColumnIndexOrThrow(_cursor, "totalOperations");
          final int _cursorIndexOfSucceededCount = CursorUtil.getColumnIndexOrThrow(_cursor, "succeededCount");
          final int _cursorIndexOfFailedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "failedCount");
          final int _cursorIndexOfIsReverted = CursorUtil.getColumnIndexOrThrow(_cursor, "isReverted");
          final int _cursorIndexOfRevertedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "revertedAt");
          final RenameBatchEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpServerId;
            _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            final String _tmpServerName;
            if (_cursor.isNull(_cursorIndexOfServerName)) {
              _tmpServerName = null;
            } else {
              _tmpServerName = _cursor.getString(_cursorIndexOfServerName);
            }
            final String _tmpBatchName;
            if (_cursor.isNull(_cursorIndexOfBatchName)) {
              _tmpBatchName = null;
            } else {
              _tmpBatchName = _cursor.getString(_cursorIndexOfBatchName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmpTotalOperations;
            _tmpTotalOperations = _cursor.getInt(_cursorIndexOfTotalOperations);
            final int _tmpSucceededCount;
            _tmpSucceededCount = _cursor.getInt(_cursorIndexOfSucceededCount);
            final int _tmpFailedCount;
            _tmpFailedCount = _cursor.getInt(_cursorIndexOfFailedCount);
            final boolean _tmpIsReverted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsReverted);
            _tmpIsReverted = _tmp != 0;
            final Long _tmpRevertedAt;
            if (_cursor.isNull(_cursorIndexOfRevertedAt)) {
              _tmpRevertedAt = null;
            } else {
              _tmpRevertedAt = _cursor.getLong(_cursorIndexOfRevertedAt);
            }
            _result = new RenameBatchEntity(_tmpId,_tmpServerId,_tmpServerName,_tmpBatchName,_tmpCreatedAt,_tmpTotalOperations,_tmpSucceededCount,_tmpFailedCount,_tmpIsReverted,_tmpRevertedAt);
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

  @Override
  public Object getEntries(final long batchId,
      final Continuation<? super List<RenameEntryEntity>> $completion) {
    final String _sql = "SELECT * FROM rename_entries WHERE batchId = ? ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, batchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RenameEntryEntity>>() {
      @Override
      @NonNull
      public List<RenameEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "batchId");
          final int _cursorIndexOfSourcePath = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePath");
          final int _cursorIndexOfTargetPath = CursorUtil.getColumnIndexOrThrow(_cursor, "targetPath");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaType");
          final int _cursorIndexOfCompanionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "companionsJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<RenameEntryEntity> _result = new ArrayList<RenameEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RenameEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBatchId;
            _tmpBatchId = _cursor.getLong(_cursorIndexOfBatchId);
            final String _tmpSourcePath;
            if (_cursor.isNull(_cursorIndexOfSourcePath)) {
              _tmpSourcePath = null;
            } else {
              _tmpSourcePath = _cursor.getString(_cursorIndexOfSourcePath);
            }
            final String _tmpTargetPath;
            if (_cursor.isNull(_cursorIndexOfTargetPath)) {
              _tmpTargetPath = null;
            } else {
              _tmpTargetPath = _cursor.getString(_cursorIndexOfTargetPath);
            }
            final String _tmpMediaType;
            if (_cursor.isNull(_cursorIndexOfMediaType)) {
              _tmpMediaType = null;
            } else {
              _tmpMediaType = _cursor.getString(_cursorIndexOfMediaType);
            }
            final String _tmpCompanionsJson;
            if (_cursor.isNull(_cursorIndexOfCompanionsJson)) {
              _tmpCompanionsJson = null;
            } else {
              _tmpCompanionsJson = _cursor.getString(_cursorIndexOfCompanionsJson);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new RenameEntryEntity(_tmpId,_tmpBatchId,_tmpSourcePath,_tmpTargetPath,_tmpMediaType,_tmpCompanionsJson,_tmpStatus,_tmpErrorMessage);
            _result.add(_item);
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
