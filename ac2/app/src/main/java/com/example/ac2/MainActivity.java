package com.example.ac2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ac2.adapter.TreinoAdapter;
import com.example.ac2.database.TreinoDatabase;
import com.example.ac2.models.Treino;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Views do formulário
    private EditText etNomeTreino, etDuracao, etData;
    private Spinner spinnerTipoAtividade, spinnerIntensidade;
    private CheckBox checkTreino;
    private Button btnSalvar;

    // Views de filtro/lista
    private Spinner spinnerFiltro, spinnerOrdem;
    private CheckBox checkFiltroTreinou;
    private ListView listView;

    // Dados
    private TreinoDatabase db;
    private TreinoAdapter adapter;
    private String idEditando = null; // null = novo registro

    private final String[] ATIVIDADE = {"Musculação", "Corrida", "Caminhada",
            "Ciclismo", "Funcional"};
    private final String[] TREINO_ATIVIDADE;
    private final String[] ORDENS = {"NomeTitulo", "Duracao", "Data"};
    private final String[] INTENSIDADE = {"Leve", "Moderada", "Intensa"};

    public MainActivity() {
        TREINO_ATIVIDADE = new String[ATIVIDADE.length + 1];
        TREINO_ATIVIDADE[0] = "Todos";
        System.arraycopy(ATIVIDADE, 0, TREINO_ATIVIDADE, 1, ATIVIDADE.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new TreinoDatabase();
        vincularViews();
        configurarSpinners();
        configurarBotaoSalvar();
        configurarLista();
        atualizarLista();
    }

    private void vincularViews() {
        etNomeTreino          = findViewById(R.id.etNomeTreino);
        etDuracao             = findViewById(R.id.etDuracao);
        etData                = findViewById(R.id.etData);
        spinnerTipoAtividade     = findViewById(R.id.spinnerTipoAtividade);
        spinnerIntensidade = findViewById(R.id.spinnerIntensidade);
        checkTreino        = findViewById(R.id.checkTreinou);
        btnSalvar             = findViewById(R.id.btnSalvar);
        spinnerFiltro         = findViewById(R.id.spinnerFiltro);
        spinnerOrdem          = findViewById(R.id.spinnerOrdem);
        checkFiltroTreinou      = findViewById(R.id.checkFiltroTreino);
        listView              = findViewById(R.id.listViewTreinos);
    }

    private void configurarSpinners() {
        spinnerTipoAtividade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ATIVIDADE));

        spinnerFiltro.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, TREINO_ATIVIDADE));

        spinnerIntensidade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, INTENSIDADE));

        spinnerOrdem.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ORDENS));

        AdapterView.OnItemSelectedListener reload = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long l) { atualizarLista(); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerFiltro.setOnItemSelectedListener(reload);
        spinnerOrdem.setOnItemSelectedListener(reload);
        checkFiltroTreinou.setOnCheckedChangeListener((b, c) -> atualizarLista());
    }

    private void configurarBotaoSalvar() {
        btnSalvar.setOnClickListener(v -> {
            String nomeTreino = etNomeTreino.getText().toString().trim();
            if (nomeTreino.isEmpty()) {
                Toast.makeText(this, "Nome do Treino é obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }

            String duracaoStr = etDuracao.getText().toString().trim();
            if (duracaoStr.isEmpty()) {
                Toast.makeText(this, "Duração de treino é obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }

            String dataStr = etData.getText().toString().trim();
            if (dataStr.isEmpty()) {
                Toast.makeText(this, "Data do treino é obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }

            String tipoAtividade  = spinnerTipoAtividade.getSelectedItem().toString();
            double duracao     = Double.parseDouble(duracaoStr);
            String data       = etData.getText().toString().trim();
            String intensidade  = spinnerIntensidade.getSelectedItem().toString();
            boolean treinoConcluido  = checkTreino.isChecked();

            if (idEditando == null) {
                db.inserir(new Treino(nomeTreino, tipoAtividade, duracao, data, intensidade, treinoConcluido));
                Toast.makeText(this, "Atividade salva!", Toast.LENGTH_SHORT).show();
            } else {
                db.atualizar(new Treino(idEditando, nomeTreino, tipoAtividade, duracao, data, intensidade, treinoConcluido));
                Toast.makeText(this, "Atividade atualizada!", Toast.LENGTH_SHORT).show();
                idEditando = null;
                btnSalvar.setText("Salvar");
            }

            limparFormulario();
            atualizarLista();
        });
    }

    private void configurarLista() {
        adapter = new TreinoAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        // Clique simples — carrega para edição
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Treino t = adapter.getItem(position);
            idEditando = t.getId();

            etNomeTreino.setText(t.getNomeTreino());
            etDuracao.setText(String.valueOf(t.getDuracao()));
            etData.setText(t.getData());
            checkTreino.setChecked(t.isTreinoConcluido());
            btnSalvar.setText("Atualizar");

            for (int i = 0; i < ATIVIDADE.length; i++) {
                if (ATIVIDADE[i].equals(t.getTipoAtividade())) {
                    spinnerTipoAtividade.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < INTENSIDADE.length; i++) {
                if (INTENSIDADE[i].equals(t.getIntensidade())) {
                    spinnerIntensidade.setSelection(i);
                    break;
                }
            }
        });

        // Clique longo — confirma exclusão
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Treino t = adapter.getItem(position);
            new AlertDialog.Builder(this)
                    .setTitle("Excluir")
                    .setMessage("Deseja excluir \"" + t.getNomeTreino() + "\"?")
                    .setPositiveButton("Excluir", (dialog, w) -> {
                        db.deletar(t.getId());
                        atualizarLista();
                        Toast.makeText(this, "Excluído", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
    }

    private void atualizarLista() {
        String tipoAtividadeFiltro = spinnerFiltro.getSelectedItem().toString();
        if ("Todos".equals(tipoAtividadeFiltro)) tipoAtividadeFiltro = null;
        boolean treinou    = checkFiltroTreinou.isChecked();
        String ordemStr = spinnerOrdem.getSelectedItem().toString();

        String ordem = null;
        if ("Duracao".equals(ordemStr)) ordem = "Duracao";
        if ("Data".equals(ordemStr))  ordem = "Data";

        String finalCategoria = tipoAtividadeFiltro;
        String finalOrdem     = ordem;

        db.listar(finalCategoria, treinou, finalOrdem, lista ->
                runOnUiThread(() -> adapter.atualizar(lista))
        );
    }

    private void limparFormulario() {
        etNomeTreino.setText("");
        spinnerTipoAtividade.setSelection(0);
        etDuracao.setText("");
        etData.setText("");
        spinnerIntensidade.setSelection(0);
        checkTreino.setChecked(false);
    }

}