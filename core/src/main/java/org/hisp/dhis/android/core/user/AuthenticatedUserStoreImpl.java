package org.hisp.dhis.android.core.user;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.data.database.DbOpenHelper.Tables;
import org.hisp.dhis.android.core.user.AuthenticatedUserContract.Columns;

import java.util.ArrayList;
import java.util.List;

public class AuthenticatedUserStoreImpl implements AuthenticatedUserStore {
    private static final String[] PROJECTION = new String[]{
            Columns.ID, Columns.USER, Columns.CREDENTIALS
    };

    private static final String INSERT_STATEMENT = "INSERT INTO " + Tables.AUTHENTICATED_USER +
            " (" + Columns.USER + ", " + Columns.CREDENTIALS + ")" +
            " VALUES (?, ?);";

    private final SQLiteDatabase sqLiteDatabase;
    private final SQLiteStatement insertRowStatement;

    public AuthenticatedUserStoreImpl(@NonNull SQLiteDatabase database) {
        this.sqLiteDatabase = database;
        this.insertRowStatement = database.compileStatement(INSERT_STATEMENT);
    }

    @Override
    public long insert(@NonNull String userUid, @NonNull String credentials) {
        insertRowStatement.clearBindings();
        insertRowStatement.bindString(1, userUid);
        insertRowStatement.bindString(2, credentials);
        return insertRowStatement.executeInsert();
    }

    @NonNull
    @Override
    public List<AuthenticatedUserModel> query() {
        List<AuthenticatedUserModel> rows = new ArrayList<>();

        Cursor queryCursor = sqLiteDatabase.query(Tables.AUTHENTICATED_USER,
                PROJECTION, null, null, null, null, null);

        if (queryCursor == null) {
            return rows;
        }

        try {
            if (queryCursor.getCount() > 0) {
                queryCursor.moveToFirst();

                do {
                    rows.add(AuthenticatedUserModel.create(queryCursor));
                } while (queryCursor.moveToNext());
            }
        } finally {
            queryCursor.close();
        }

        return rows;
    }

    @Override
    public void close() {
        insertRowStatement.close();
    }
}