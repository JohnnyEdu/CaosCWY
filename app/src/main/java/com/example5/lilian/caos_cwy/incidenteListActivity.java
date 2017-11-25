package com.example5.lilian.caos_cwy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.tasks.IncidenteSELECTTask;
import com.example5.lilian.caos_cwy.tasks.MisIncidenteDELETEUPDATETask;
import com.example5.lilian.caos_cwy.tasks.MisIncidenteSELECTTask;
import com.example5.lilian.caos_cwy.utils.Utilidades;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static boolean isMisIncidentesView = false;
    private static Activity mainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicializo el contenedor de los incidentes del maestro - detalle

        isMisIncidentesView = true;

        setContentView(R.layout.activity_incidente_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.incidente_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("sesion",MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario","");

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.cargandoIncidentes);
        //traer los incidentes para mostrar
        MisIncidenteSELECTTask selct = new MisIncidenteSELECTTask();
        selct.setUsuario(usuario);
        selct.setActivity(this);
        selct.setProgressBar(progressBar);
        selct.execute();
        mainactivity = this;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IncidentesContent.reset();
        isMisIncidentesView = false;
        //Toast.makeText(getApplicationContext(),"Se destruye",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Activity mParentActivity;
        private List<Incidente> itemslistado = new ArrayList<>();
        private boolean agruparItems;
        private String filtro;
        private Map<String,List<Incidente>> itemsAgrupados;

        public SimpleItemRecyclerViewAdapter(Activity parent,
                                      List<Incidente> items,boolean agruparItemsParam,String filtro) {
                mParentActivity = parent;
                if(!"".equals(filtro) && !"Todos".equals(filtro) &&  !IncidentesContent.INCIDENTES_AGRUPADOS.isEmpty()){
                    itemslistado = IncidentesContent.INCIDENTES_AGRUPADOS.get(filtro);
                }
                else{
                    itemslistado = items;
                }
                agruparItems = agruparItemsParam;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = null;
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_incidentes, parent, false);
                return new ViewHolder(view);
            }


        public String getFiltro() {
            return filtro;
        }

        public void setFiltro(String filtro) {
            this.filtro = filtro;
        }

        @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                /***YA NO SE USA AGRUPADOS DESDE LA BASE*
                 * agruparItems viene en true solo cuando filtra, el filtro muestra colapsado
                 * **/
                    final Incidente incidente = itemslistado.get(position);
                    holder.mIdView.setText(incidente.getTipo());
                    LayoutInflater vi = (LayoutInflater) mParentActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    //a cada item del detalle de cada grupo, le pongo en su onclick que me lleve al activity del detalle
                    holder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //mTwoPane, pareciera que se usa para cuando no queres que el detalle te cambie de activity, sino de fragment
                            //por ahora esta hecho con activity, asi que entra al else siempre
                            Context context = mParentActivity;
                            Intent intent = new Intent(context, incidenteDetailActivity.class);
                            //le paso
                            intent.putExtra("item_id", incidente.getId());
                            intent.putExtra(incidenteDetailActivity.ARG_ITEM_ID, String.valueOf(incidente.getId()));
                            // Integer itemid = intent.getExtras().getInt("item_id");
                            context.startActivity(intent);
                        }
                    });

                    ViewGroup detalle = (ViewGroup)holder.mContentView.findViewById(R.id.itemDetalleMaestro);
                    TextView texto = (TextView) detalle.findViewById(R.id.content);
                        texto.setText(incidente.getComentario());
                    TextView textoFecha = (TextView) detalle.findViewById(R.id.fechacontent);
                        textoFecha .setText(incidente.getFechaYhora());
                    if(incidente.getCaptura()!=null) {
                        ImageView imagen = (ImageView) detalle.findViewById(R.id.imagenMaestroDetalle);
                        imagen.setImageBitmap(incidente.getCaptura().getImagen());
                    }
                    if(isMisIncidentesView){
                        ImageButton editarbtn = (ImageButton)detalle.findViewById(R.id.editar);
                        editarbtn.setVisibility(View.VISIBLE);
                        editarbtn.setOnClickListener(new View.OnClickListener() {
                            private Incidente incidentecla = incidente;

                            @Override
                            public void onClick(View v) {
                                Intent pantallaEditar = new Intent(mainactivity.getApplicationContext(),EditarIncidenteActivity.class);
                                mParentActivity.startActivity(pantallaEditar);
                            }
                        });
                        ImageButton eliminarbtn = (ImageButton)detalle.findViewById(R.id.eliminar);
                        eliminarbtn.setVisibility(View.VISIBLE);
                        eliminarbtn.setOnClickListener(new View.OnClickListener() {
                            private Incidente incidentecla = incidente;
                            @Override
                            public void onClick(View v) {
                                ProgressBar progressBar = (ProgressBar)mainactivity.findViewById(R.id.cargandoIncidentes);
                                MisIncidenteDELETEUPDATETask eliminar = new MisIncidenteDELETEUPDATETask("DELETE");
                                eliminar.setActivity(mainactivity);
                                eliminar.setProgressBar(progressBar );
                                eliminar.setIncidente(incidente);
                                eliminar.execute();
                            }
                        });
                    }

            }

            @Override
            public int getItemCount() {
                return itemslistado.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            ViewGroup mContentView;

            private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View detalle = mContentView.findViewById(R.id.itemDetalleMaestro);
                    boolean isVisible = detalle.getVisibility() == View.VISIBLE;
                    if(isVisible){
                        detalle.setVisibility(View.GONE);
                    }else{
                        detalle.setVisibility(View.VISIBLE);
                    }
                }
            };

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (ViewGroup) view.findViewById(R.id.contenedorItemMaestro);
                if(mIdView!=null) {
                    mIdView.setOnClickListener(mOnClickListener);
                }
            }
        }
    }
}
