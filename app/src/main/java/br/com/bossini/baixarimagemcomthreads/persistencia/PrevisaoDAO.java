package br.com.bossini.baixarimagemcomthreads.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.bossini.baixarimagemcomthreads.Previsao;

/**
 * Created by rodrigo on 04/04/18.
 */

public class PrevisaoDAO {

    private Context context;

    public PrevisaoDAO (Context context){
        this.context = context;
    }

    public long inserir (Previsao previsao){
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //icone VARCHAR(10));
        cv.put("descricao", previsao.getDescricao());
        cv.put ("dia_semana", previsao.getDiaDaSemana());
        cv.put("nome_cidade", previsao.getNomeCidade());
        cv.put("min", previsao.getMin());
        cv.put("max",previsao.getMax());
        cv.put("humidade", previsao.getHumidade());
        cv.put("icone", previsao.getIcone());
        long id = db.insert("previsao", null, cv);
        db.close();
        return id;
    }

    public List<Previsao> buscarTodos() {
        List <Previsao> previsoes = new ArrayList <>();
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c =
                db.query("previsao", null,
                        null, null, null, null, null);
        while (c.moveToNext()){
            Previsao p = new Previsao();

            String descricao = c.getString(
                    c.getColumnIndexOrThrow("descricao"));
            p.setDescricao(descricao);

            String diaSemana = c.getString(
                    c.getColumnIndexOrThrow("dia_semana"));
            p.setDiaDaSemana(diaSemana);

            String nomeCidade = c.getString(
                    c.getColumnIndexOrThrow("nome_cidade")
            );
            p.setNomeCidade(nomeCidade);

            double min = c.getDouble(c.getColumnIndexOrThrow("min"));
            p.setMin(min);

            double max = c.getDouble(c.getColumnIndexOrThrow("max"));
            p.setMax(max);

            int humidade = c.getInt(c.getColumnIndexOrThrow("humidade"));
            p.setHumidade(humidade);

            String icone = c.getString(c.getColumnIndexOrThrow("icone"));
            p.setIcone(icone);

            previsoes.add(p);
        }
        c.close();
        db.close();
        return previsoes;
    }
}
