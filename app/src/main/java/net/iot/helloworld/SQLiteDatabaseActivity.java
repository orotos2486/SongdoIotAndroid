package net.iot.helloworld;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLiteDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data);
        readDatabase();
    }
    public void addRecord(View view) {
        EditText wordText = (EditText)findViewById(R.id.word);
        EditText definitionText = (EditText)findViewById(R.id.definition);
        writeDatabase(wordText.getText().toString(), definitionText.getText().toString());
        readDatabase();
        wordText.setText("");
        definitionText.setText("");
    }

    public void removeDatabase(int _id) {
        Dictionary dictionary = new Dictionary(SQLiteDatabaseActivity.this);
        SQLiteDatabase db = dictionary.getWritableDatabase();
        String[] args = {_id+""};
        db.delete(Dictionary.TABLE_NAME, "_id = ?", args);
    }
    public void writeDatabase(String word, String definition) {
        Dictionary dictionary = new Dictionary(SQLiteDatabaseActivity.this);
        SQLiteDatabase db = dictionary.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Dictionary.WORD, word);
        values.put(Dictionary.DEFINITION, definition);
        db.insert(Dictionary.TABLE_NAME, null, values);
    }
    class Voca {
        int _id; String word, definition;
        Voca(int _id, String word, String definition) {
            this._id = _id; this.word = word; this.definition = definition;
        }
    }
    ArrayList<Voca> vocaList = new ArrayList<Voca>();
    public void readDatabase() {
        Dictionary dictionary = new Dictionary(SQLiteDatabaseActivity.this);
        SQLiteDatabase db = dictionary.getReadableDatabase();
        String sql = "select * from "+Dictionary.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        vocaList.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            int _id = cursor.getInt(0);
            String word = cursor.getString(1);
            String definition = cursor.getString(2);
            vocaList.add(new Voca(_id, word, definition));
        }
        cursor.close();
        //-------------------------------------------------
        String items[] = new String[vocaList.size()];
        for (int i = 0; i < vocaList.size(); i++) {
            items[i] = vocaList.get(i).word + " ("+ vocaList.get(i).definition +")";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SQLiteDatabaseActivity.this, android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SQLiteDatabaseActivity.this, position+"", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;//!!!!!!!!!
                AlertDialog.Builder dialog = new AlertDialog.Builder(SQLiteDatabaseActivity.this);
                dialog.setMessage("해당 데이터를 삭제하시겠습니까?");
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeDatabase(vocaList.get(pos)._id);
                        readDatabase();
                    }
                });
                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                dialog.show();
                return true;
            }
        });
    }
}