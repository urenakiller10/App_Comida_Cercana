package com.example.crowfunding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.crowfunding.R; // Importa el archivo R de tu paquete


public class crearProyecto extends AppCompatActivity {

    private EditText nombreProyecto;
    private EditText descripcionProyecto;
    private EditText fechaLimite;
    private EditText objetivoFinanciacion;
    private Spinner spinnerCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_crearproyecto);

        // Inicializar los componentes
        nombreProyecto = findViewById(R.id.nombre_proyecto);
        descripcionProyecto = findViewById(R.id.descripcion_proyecto);
        fechaLimite = findViewById(R.id.fecha_limite);
        objetivoFinanciacion = findViewById(R.id.objetivo_financiacion);
        spinnerCategoria = findViewById(R.id.spinner_categoria);

        // Configurar el Spinner (ComboBox) para categorías
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Inicializar el botón para crear el proyecto
        Button crearProyectoButton = findViewById(R.id.btn_crear_proyecto);
        crearProyectoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes manejar la lógica para crear el proyecto
                // Por ejemplo, obtener los valores de los EditText y el Spinner
                String nombre = nombreProyecto.getText().toString();
                String descripcion = descripcionProyecto.getText().toString();
                String fecha = fechaLimite.getText().toString();
                String objetivo = objetivoFinanciacion.getText().toString();
                String categoria = spinnerCategoria.getSelectedItem().toString();

                // Lógica para procesar el nuevo proyecto (por ejemplo, guardarlo en Firebase)
            }
        });
    }
}
