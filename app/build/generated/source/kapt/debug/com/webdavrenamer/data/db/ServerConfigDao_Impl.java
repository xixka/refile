package com.webdavrenamer.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
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
public final class ServerConfigDao_Impl implements ServerConfigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ServerConfigEntity> __insertionAdapterOfServerConfigEntity;

  private final EntityDeletionOrUpdateAdapter<ServerConfigEntity> __deletionAdapterOfServerConfigEntity;

  private final EntityDeletionOrUpdateAdapter<ServerConfigEntity> __updateAdapterOfServerConfigEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public ServerConfigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfServerConfigEntity = new EntityInsertionAdapter<ServerConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `server_configs` (`id`,`name`,`baseUrl`,`port`,`rootPath`,`username`,`encryptedPassword`,`authType`,`https`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServerConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getBaseUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBaseUrl());
        }
        if (entity.getPort() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getPort());
        }
        if (entity.getRootPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRootPath());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUsername());
        }
        if (entity.getEncryptedPassword() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getEncryptedPassword());
        }
        if (entity.getAuthType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAuthType());
        }
        final int _tmp = entity.getHttps() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfServerConfigEntity = new EntityDeletionOrUpdateAdapter<ServerConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `server_configs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServerConfigEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfServerConfigEntity = new EntityDeletionOrUpdateAdapter<ServerConfigEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `server_configs` SET `id` = ?,`name` = ?,`baseUrl` = ?,`port` = ?,`rootPath` = ?,`username` = ?,`encryptedPassword` = ?,`authType` = ?,`https` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ServerConfigEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getBaseUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBaseUrl());
        }
        if (entity.getPort() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getPort());
        }
        if (entity.getRootPath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRootPath());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUsername());
        }
        if (entity.getEncryptedPassword() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getEncryptedPassword());
        }
        if (entity.getAuthType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAuthType());
        }
        final int _tmp = entity.getHttps() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM server_configs WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ServerConfigEntity server,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfServerConfigEntity.insertAndReturnId(server);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ServerConfigEntity server,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfServerConfigEntity.handle(server);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ServerConfigEntity server,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfServerConfigEntity.handle(server);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ServerConfigEntity>> observeAll() {
    final String _sql = "SELECT * FROM server_configs ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"server_configs"}, new Callable<List<ServerConfigEntity>>() {
      @Override
      @NonNull
      public List<ServerConfigEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfRootPath = CursorUtil.getColumnIndexOrThrow(_cursor, "rootPath");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfAuthType = CursorUtil.getColumnIndexOrThrow(_cursor, "authType");
          final int _cursorIndexOfHttps = CursorUtil.getColumnIndexOrThrow(_cursor, "https");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ServerConfigEntity> _result = new ArrayList<ServerConfigEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ServerConfigEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpBaseUrl;
            if (_cursor.isNull(_cursorIndexOfBaseUrl)) {
              _tmpBaseUrl = null;
            } else {
              _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            }
            final Integer _tmpPort;
            if (_cursor.isNull(_cursorIndexOfPort)) {
              _tmpPort = null;
            } else {
              _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            }
            final String _tmpRootPath;
            if (_cursor.isNull(_cursorIndexOfRootPath)) {
              _tmpRootPath = null;
            } else {
              _tmpRootPath = _cursor.getString(_cursorIndexOfRootPath);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            final String _tmpAuthType;
            if (_cursor.isNull(_cursorIndexOfAuthType)) {
              _tmpAuthType = null;
            } else {
              _tmpAuthType = _cursor.getString(_cursorIndexOfAuthType);
            }
            final boolean _tmpHttps;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHttps);
            _tmpHttps = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ServerConfigEntity(_tmpId,_tmpName,_tmpBaseUrl,_tmpPort,_tmpRootPath,_tmpUsername,_tmpEncryptedPassword,_tmpAuthType,_tmpHttps,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getById(final long id, final Continuation<? super ServerConfigEntity> $completion) {
    final String _sql = "SELECT * FROM server_configs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ServerConfigEntity>() {
      @Override
      @Nullable
      public ServerConfigEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfRootPath = CursorUtil.getColumnIndexOrThrow(_cursor, "rootPath");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfAuthType = CursorUtil.getColumnIndexOrThrow(_cursor, "authType");
          final int _cursorIndexOfHttps = CursorUtil.getColumnIndexOrThrow(_cursor, "https");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ServerConfigEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpBaseUrl;
            if (_cursor.isNull(_cursorIndexOfBaseUrl)) {
              _tmpBaseUrl = null;
            } else {
              _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            }
            final Integer _tmpPort;
            if (_cursor.isNull(_cursorIndexOfPort)) {
              _tmpPort = null;
            } else {
              _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            }
            final String _tmpRootPath;
            if (_cursor.isNull(_cursorIndexOfRootPath)) {
              _tmpRootPath = null;
            } else {
              _tmpRootPath = _cursor.getString(_cursorIndexOfRootPath);
            }
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            final String _tmpAuthType;
            if (_cursor.isNull(_cursorIndexOfAuthType)) {
              _tmpAuthType = null;
            } else {
              _tmpAuthType = _cursor.getString(_cursorIndexOfAuthType);
            }
            final boolean _tmpHttps;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHttps);
            _tmpHttps = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ServerConfigEntity(_tmpId,_tmpName,_tmpBaseUrl,_tmpPort,_tmpRootPath,_tmpUsername,_tmpEncryptedPassword,_tmpAuthType,_tmpHttps,_tmpCreatedAt,_tmpUpdatedAt);
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
