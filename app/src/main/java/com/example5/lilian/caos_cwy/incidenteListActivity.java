package com.example5.lilian.caos_cwy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.fragments.incidenteDetailFragment;
import com.example5.lilian.caos_cwy.tasks.ImagenesSELECTTask;
import com.example5.lilian.caos_cwy.tasks.IncidenteSELECTTask;

import java.util.List;

/**
 * An activity representing a list of incidentes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link incidenteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class incidenteListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicializo el contenedor de los incidentes del maestro - detalle
        IncidentesContent inc = new IncidentesContent();

        setContentView(R.layout.activity_incidente_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        //Boton flotante
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.incidente_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        ProgressBar progressBar = (ProgressBar)findViewById(R.id.cargandoIncidentes);
        //traer los incidentes para mostrar
        IncidenteSELECTTask selct = new IncidenteSELECTTask();
        selct.setActivity(this);
        selct.setProgressBar(progressBar);
        selct.execute(mTwoPane);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IncidentesContent.reset();
        //Toast.makeText(getApplicationContext(),"Se destruye",Toast.LENGTH_LONG).show();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Activity mParentActivity;
        //guardo mValues y mValuesSinAgrupar para tener el detalle de cada grupo a mano
        private final List<Incidente> mValues;
        private List<Incidente> mValuesSinAgrupar;
        private final boolean mTwoPane;

        public SimpleItemRecyclerViewAdapter(Activity parent,
                                      List<Incidente> items, List<Incidente> itemsSinAgrupar,
                                      boolean twoPane) {
            mValues = items;
            mValuesSinAgrupar = itemsSinAgrupar;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_incidentes, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
            //Faltaba pasar a toString, probee con setText("hola") y andaba jaj
                holder.mIdView.setText(  mValues.get(position).getTipo().toString() + "   ("+ mValues.get(position).getCantidad().toString() +")");
                //este método "onBindViewHolder" contiene cada uno de los items agrupados, es decir cada item que usa la RecyclerView
                //por cada uno de los items que se va generando en la vista, le seteo su detalle sacando de mValuesSinAgrupar y localizando por tipo
                //de agrupamiento ej: "ACCIDENTE" sería uno de los que entra a este método y por cada elemento en mValuesSinAgrupar, muestro los ACCIDENTE
                LayoutInflater vi = (LayoutInflater) mParentActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Calendar cal= Calendar.getInstance();
                for(final Incidente incidente: mValuesSinAgrupar){
                    if(incidente.getTipo().equalsIgnoreCase(mValues.get(position).getTipo())){
                        TextView v = (TextView)vi.inflate(R.layout.item_incidentes_agrupados_content, null);
                        v.setTag(incidente);
                        v.setText("Fecha: "+ incidente.getFechaYhora());
                        //a cada item del detalle de cada grupo, le pongo en su onclick que me lleve al activity del detalle
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Incidente incidenteSelec =(Incidente) view.getTag();
                                //mTwoPane, pareciera que se usa para cuando no queres que el detalle te cambie de activity, sino de fragment
                                //por ahora esta hecho con activity, asi que entra al else siempre
                                if (mTwoPane) {
                                    /*Bundle arguments = new Bundle();
                                    arguments.putString(incidenteDetailActivity.ARG_ITEM_ID, String.valueOf(incidenteSelec.getId()));
                                    incidenteDetailFragment fragment = new incidenteDetailFragment();
                                    fragment.setArguments(arguments);
                                       mParentActivity.getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.incidente_detail_container, fragment)
                                            .commit();*/
                                } else {
                                    Context context = mParentActivity;
                                    Intent intent = new Intent(context, incidenteDetailActivity.class);
                                    //le paso
                                    intent.putExtra("item_id", incidenteSelec.getId());
                                    intent.putExtra(incidenteDetailActivity.ARG_ITEM_ID, String.valueOf(incidenteSelec.getId()));
                                   // Integer itemid = intent.getExtras().getInt("item_id");
                                    context.startActivity(intent);
                                }
                            }
                        });

                        holder.mContentView.addView(v);
                    }
                }
                holder.mContentView.setVisibility(View.GONE);
                //holder.mIdView.setTag(mValues.get(position));

            }

            @Override
            public int getItemCount() {
                return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final LinearLayout mContentView;
            TextView mItemDetail = null;

            private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity contenedor = (Activity) view.getContext();
                    boolean isVisible = mContentView.getVisibility() == View.VISIBLE;
                    if(isVisible){
                        mContentView.setVisibility(View.GONE);
                    }else{
                        mContentView.setVisibility(View.VISIBLE);
                    }
                }
            };



            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mItemDetail = (TextView) view.findViewById(R.id.content);
                mContentView = (LinearLayout) view.findViewById(R.id.contenedorDetalle);
                mIdView.setOnClickListener(mOnClickListener);
            }
        }
    }
}
