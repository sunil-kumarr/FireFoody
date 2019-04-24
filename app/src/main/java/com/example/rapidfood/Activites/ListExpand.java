//package com.example.rapidfood.Activites;
//
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//import com.material.components.C0445R;
//import com.material.components.adapter.AdapterListExpand;
//import com.material.components.adapter.AdapterListExpand.OnItemClickListener;
//import com.material.components.data.DataGenerator;
//import com.material.components.model.Social;
//import com.material.components.utils.Tools;
//import com.material.components.widget.LineItemDecoration;
//
//public class ListExpand extends AppCompatActivity {
//    private AdapterListExpand mAdapter;
//    private View parent_view;
//    private RecyclerView recyclerView;
//
//    /* renamed from: com.material.components.activity.list.ListExpand$1 */
//    class C10751 implements OnItemClickListener {
//        C10751() {
//        }
//
//        public void onItemClick(View view, Social social, int i) {
//            view = ListExpand.this.parent_view;
//            i = new StringBuilder();
//            i.append("Item ");
//            i.append(social.name);
//            i.append(" clicked");
//            Snackbar.make(view, i.toString(), -1).show();
//        }
//    }
//
//    protected void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
//        setContentView((int) C0445R.layout.activity_list_expand);
//        this.parent_view = findViewById(16908290);
//        initToolbar();
//        initComponent();
//    }
//
//    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(C0445R.id.toolbar);
//        toolbar.setNavigationIcon((int) C0445R.drawable.ic_menu);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle((CharSequence) "Expandable");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this);
//    }
//
//    private void initComponent() {
//        this.recyclerView = (RecyclerView) findViewById(C0445R.id.recyclerView);
//        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        this.recyclerView.addItemDecoration(new LineItemDecoration(this, 1));
//        this.recyclerView.setHasFixedSize(true);
//        this.mAdapter = new AdapterListExpand(this, DataGenerator.getSocialData(this));
//        this.recyclerView.setAdapter(this.mAdapter);
//        this.mAdapter.setOnItemClickListener(new C10751());
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(C0445R.menu.menu_search_setting, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == 16908332) {
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), menuItem.getTitle(), 0).show();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }
//}
