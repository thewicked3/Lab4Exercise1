package th.ac.tu.siit.its333.lab4exercise1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    CourseDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

        helper = new CourseDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(credit) cr, SUM(credit*value) gp FROM course;",
                null);
        cursor.moveToFirst();

        Double cr  = cursor.getDouble(0);
        Double gp  = cursor.getDouble(1);
        double gpa = gp/cr;

        TextView tvGP =(TextView) findViewById(R.id.tvGP);
        tvGP.setText(Double.toString(gp));
        TextView tvCR =(TextView) findViewById(R.id.tvCR);
        tvCR.setText(Double.toString(cr));
        TextView tvGPA =(TextView) findViewById(R.id.tvGPA);
        tvGPA.setText(Double.toString(gpa));




        // This method is called when this activity is put foreground.

    }

    public void buttonClicked(View v) {
        int id = v.getId();
        Intent i;

        switch(id) {
            case R.id.btAdd:
                i = new Intent(this, AddCourseActivity.class);
                startActivityForResult(i, 88);
                break;

            case R.id.btShow:
                i = new Intent(this, ListCourseActivity.class);
                startActivity(i);
                break;

            case R.id.btReset:
                SQLiteDatabase db = helper.getReadableDatabase();
                int n_rows = db.delete("course","",null);
               TextView tvGP = (TextView) findViewById(R.id.tvGP);
                tvGP.setText("0.0");
                TextView tvCR = (TextView) findViewById(R.id.tvCR);
                tvCR.setText("0.0");
                TextView tvGPA = (TextView) findViewById(R.id.tvGPA);
                tvGPA.setText("0.0");

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 88) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("code");
                int credit = data.getIntExtra("credit", 0);
                String grade = data.getStringExtra("grade");

                helper = new CourseDBHelper(this.getApplicationContext());
                SQLiteDatabase dbw = helper.getWritableDatabase();
                ContentValues r = new ContentValues();
                r.put("code", code);
                r.put("credit", credit);
                r.put("grade", grade);
                r.put("value",gradeToValue(grade));

                long new_id = dbw.insert("course", null, r);

                onResume();

            }
        }


        Log.d("course", "onActivityResult");
    }

    double gradeToValue(String g) {
        if (g.equals("A"))
            return 4.0;
        else if (g.equals("B+"))
            return 3.5;
        else if (g.equals("B"))
            return 3.0;
        else if (g.equals("C+"))
            return 2.5;
        else if (g.equals("C"))
            return 2.0;
        else if (g.equals("D+"))
            return 1.5;
        else if (g.equals("D"))
            return 1.0;
        else
            return 0.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
