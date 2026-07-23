package com.webdavrenamer.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ServerConfigDao _serverConfigDao;

  private volatile RenameBatchDao _renameBatchDao;

  private volatile TmdbCacheDao _tmdbCacheDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `server_configs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `baseUrl` TEXT NOT NULL, `port` INTEGER, `rootPath` TEXT NOT NULL, `username` TEXT, `encryptedPassword` TEXT, `authType` TEXT NOT NULL, `https` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rename_batches` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serverId` INTEGER NOT NULL, `serverName` TEXT NOT NULL, `batchName` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `totalOperations` INTEGER NOT NULL, `succeededCount` INTEGER NOT NULL, `failedCount` INTEGER NOT NULL, `isReverted` INTEGER NOT NULL, `revertedAt` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rename_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `batchId` INTEGER NOT NULL, `sourcePath` TEXT NOT NULL, `targetPath` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `companionsJson` TEXT NOT NULL, `status` TEXT NOT NULL, `errorMessage` TEXT, FOREIGN KEY(`batchId`) REFERENCES `rename_batches`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rename_entries_batchId` ON `rename_entries` (`batchId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tmdb_cache` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cacheKey` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `tmdbId` INTEGER NOT NULL, `language` TEXT NOT NULL, `seasonNumber` INTEGER, `responseJson` TEXT NOT NULL, `cachedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tmdb_cache_cacheKey` ON `tmdb_cache` (`cacheKey`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'eee072f1a25a41cfc4e0b5ac472cc6cd')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `server_configs`");
        db.execSQL("DROP TABLE IF EXISTS `rename_batches`");
        db.execSQL("DROP TABLE IF EXISTS `rename_entries`");
        db.execSQL("DROP TABLE IF EXISTS `tmdb_cache`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsServerConfigs = new HashMap<String, TableInfo.Column>(11);
        _columnsServerConfigs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("baseUrl", new TableInfo.Column("baseUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("port", new TableInfo.Column("port", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("rootPath", new TableInfo.Column("rootPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("encryptedPassword", new TableInfo.Column("encryptedPassword", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("authType", new TableInfo.Column("authType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("https", new TableInfo.Column("https", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServerConfigs.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysServerConfigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesServerConfigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoServerConfigs = new TableInfo("server_configs", _columnsServerConfigs, _foreignKeysServerConfigs, _indicesServerConfigs);
        final TableInfo _existingServerConfigs = TableInfo.read(db, "server_configs");
        if (!_infoServerConfigs.equals(_existingServerConfigs)) {
          return new RoomOpenHelper.ValidationResult(false, "server_configs(com.webdavrenamer.data.db.ServerConfigEntity).\n"
                  + " Expected:\n" + _infoServerConfigs + "\n"
                  + " Found:\n" + _existingServerConfigs);
        }
        final HashMap<String, TableInfo.Column> _columnsRenameBatches = new HashMap<String, TableInfo.Column>(10);
        _columnsRenameBatches.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("serverId", new TableInfo.Column("serverId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("serverName", new TableInfo.Column("serverName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("batchName", new TableInfo.Column("batchName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("totalOperations", new TableInfo.Column("totalOperations", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("succeededCount", new TableInfo.Column("succeededCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("failedCount", new TableInfo.Column("failedCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("isReverted", new TableInfo.Column("isReverted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameBatches.put("revertedAt", new TableInfo.Column("revertedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRenameBatches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRenameBatches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRenameBatches = new TableInfo("rename_batches", _columnsRenameBatches, _foreignKeysRenameBatches, _indicesRenameBatches);
        final TableInfo _existingRenameBatches = TableInfo.read(db, "rename_batches");
        if (!_infoRenameBatches.equals(_existingRenameBatches)) {
          return new RoomOpenHelper.ValidationResult(false, "rename_batches(com.webdavrenamer.data.db.RenameBatchEntity).\n"
                  + " Expected:\n" + _infoRenameBatches + "\n"
                  + " Found:\n" + _existingRenameBatches);
        }
        final HashMap<String, TableInfo.Column> _columnsRenameEntries = new HashMap<String, TableInfo.Column>(8);
        _columnsRenameEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("batchId", new TableInfo.Column("batchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("sourcePath", new TableInfo.Column("sourcePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("targetPath", new TableInfo.Column("targetPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("mediaType", new TableInfo.Column("mediaType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("companionsJson", new TableInfo.Column("companionsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRenameEntries.put("errorMessage", new TableInfo.Column("errorMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRenameEntries = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRenameEntries.add(new TableInfo.ForeignKey("rename_batches", "CASCADE", "NO ACTION", Arrays.asList("batchId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRenameEntries = new HashSet<TableInfo.Index>(1);
        _indicesRenameEntries.add(new TableInfo.Index("index_rename_entries_batchId", false, Arrays.asList("batchId"), Arrays.asList("ASC")));
        final TableInfo _infoRenameEntries = new TableInfo("rename_entries", _columnsRenameEntries, _foreignKeysRenameEntries, _indicesRenameEntries);
        final TableInfo _existingRenameEntries = TableInfo.read(db, "rename_entries");
        if (!_infoRenameEntries.equals(_existingRenameEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "rename_entries(com.webdavrenamer.data.db.RenameEntryEntity).\n"
                  + " Expected:\n" + _infoRenameEntries + "\n"
                  + " Found:\n" + _existingRenameEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsTmdbCache = new HashMap<String, TableInfo.Column>(8);
        _columnsTmdbCache.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("cacheKey", new TableInfo.Column("cacheKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("mediaType", new TableInfo.Column("mediaType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("tmdbId", new TableInfo.Column("tmdbId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("language", new TableInfo.Column("language", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("seasonNumber", new TableInfo.Column("seasonNumber", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("responseJson", new TableInfo.Column("responseJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTmdbCache.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTmdbCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTmdbCache = new HashSet<TableInfo.Index>(1);
        _indicesTmdbCache.add(new TableInfo.Index("index_tmdb_cache_cacheKey", true, Arrays.asList("cacheKey"), Arrays.asList("ASC")));
        final TableInfo _infoTmdbCache = new TableInfo("tmdb_cache", _columnsTmdbCache, _foreignKeysTmdbCache, _indicesTmdbCache);
        final TableInfo _existingTmdbCache = TableInfo.read(db, "tmdb_cache");
        if (!_infoTmdbCache.equals(_existingTmdbCache)) {
          return new RoomOpenHelper.ValidationResult(false, "tmdb_cache(com.webdavrenamer.data.db.TmdbCacheEntity).\n"
                  + " Expected:\n" + _infoTmdbCache + "\n"
                  + " Found:\n" + _existingTmdbCache);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "eee072f1a25a41cfc4e0b5ac472cc6cd", "9c90d35bee4b5b124b91351bfd2582fa");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "server_configs","rename_batches","rename_entries","tmdb_cache");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `server_configs`");
      _db.execSQL("DELETE FROM `rename_batches`");
      _db.execSQL("DELETE FROM `rename_entries`");
      _db.execSQL("DELETE FROM `tmdb_cache`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ServerConfigDao.class, ServerConfigDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RenameBatchDao.class, RenameBatchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TmdbCacheDao.class, TmdbCacheDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ServerConfigDao serverConfigDao() {
    if (_serverConfigDao != null) {
      return _serverConfigDao;
    } else {
      synchronized(this) {
        if(_serverConfigDao == null) {
          _serverConfigDao = new ServerConfigDao_Impl(this);
        }
        return _serverConfigDao;
      }
    }
  }

  @Override
  public RenameBatchDao renameBatchDao() {
    if (_renameBatchDao != null) {
      return _renameBatchDao;
    } else {
      synchronized(this) {
        if(_renameBatchDao == null) {
          _renameBatchDao = new RenameBatchDao_Impl(this);
        }
        return _renameBatchDao;
      }
    }
  }

  @Override
  public TmdbCacheDao tmdbCacheDao() {
    if (_tmdbCacheDao != null) {
      return _tmdbCacheDao;
    } else {
      synchronized(this) {
        if(_tmdbCacheDao == null) {
          _tmdbCacheDao = new TmdbCacheDao_Impl(this);
        }
        return _tmdbCacheDao;
      }
    }
  }
}
