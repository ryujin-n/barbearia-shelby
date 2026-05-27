package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class dbhelper extends SQLiteOpenHelper {
    private static final String NOME_BANCO   = "barbershop.db";
    private static final int    VERSAO_BANCO = 1;

    public static final String TABELA_USUARIOS  = "usuarios";
    public static final String COL_USER_ID      = "id";
    public static final String COL_USER_NOME    = "nome";
    public static final String COL_USER_SENHA   = "senha";

    public static final String TABELA_AGEND     = "agendamentos";
    public static final String COL_AG_ID        = "id";
    public static final String COL_AG_FK_USER   = "usuario_id";
    public static final String COL_AG_SERVICO   = "servico";
    public static final String COL_AG_BARBEIRO  = "barbeiro";
    public static final String COL_AG_DATETIME  = "data_horario";

    public dbhelper(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String criarUsuarios = "CREATE TABLE " + TABELA_USUARIOS + " ("
                + COL_USER_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NOME  + " TEXT NOT NULL, "
                + COL_USER_SENHA + " TEXT NOT NULL"
                + ");";
        String criarAgendamentos = "CREATE TABLE " + TABELA_AGEND + " ("
                + COL_AG_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_AG_FK_USER  + " INTEGER NOT NULL, "
                + COL_AG_SERVICO  + " TEXT NOT NULL, "
                + COL_AG_BARBEIRO + " TEXT NOT NULL, "
                + COL_AG_DATETIME + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + COL_AG_FK_USER + ") "
                + "REFERENCES " + TABELA_USUARIOS + "(" + COL_USER_ID + ")"
                + ");";

        db.execSQL(criarUsuarios);
        db.execSQL(criarAgendamentos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_AGEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
        onCreate(db);
    }

    // CRUD dos users

    public long cadUser(String nome, String senha){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NOME, nome);
        values.put(COL_USER_SENHA, senha);
        long id = db.insert(TABELA_USUARIOS, null, values);
        db.close();
        return id;
    }

    public boolean verLogin(String nome, String senha){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABELA_USUARIOS,
                new String[]{COL_USER_ID},
                COL_USER_NOME + "=? and " + COL_USER_SENHA + "=?",
                new String[]{nome, senha},
                null, null, null
        );
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public int buscarUser(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABELA_USUARIOS,
                new String[]{COL_USER_ID},
                COL_USER_NOME + "=?",
                new String[]{nome},
                null, null, null
        );
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    // CRUD dos agendamentos

    public long criarAgendamento(int userid, String servico, String barbeiro, String dataHorario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AG_FK_USER,  userid);
        values.put(COL_AG_SERVICO,  servico);
        values.put(COL_AG_BARBEIRO, barbeiro);
        values.put(COL_AG_DATETIME, dataHorario);
        long id = db.insert(TABELA_AGEND, null, values);
        db.close();
        return id;
    }

    public Cursor listAgendamento(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABELA_AGEND,
                null,
                COL_AG_FK_USER + "=?",
                new String[]{String.valueOf(userid)},
                null, null,
                COL_AG_DATETIME + " ASC"
        );
    }
    public void delAgendamento(int agendid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABELA_AGEND, COL_AG_ID + "=?", new String[]{String.valueOf(agendid)});
        db.close();
    }
}
