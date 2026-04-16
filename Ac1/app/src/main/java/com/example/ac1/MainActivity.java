package com.example.ac1;

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
import com.example.ac1.adapter.DespesaAdapter;
import com.example.ac1.database.DespesaDatabase;
import com.example.ac1.models.Despesa;

public class MainActivity extends AppCompatActivity {

    // Views do formulário
    private EditText etDescricao, etValor, etData;
    private Spinner spinnerCategoria;

    private Spinner spinnerFormaPagamento;

    private CheckBox checkFoiPago;
    private Button btnSalvar;

    // Views de filtro/lista
    private Spinner spinnerFiltro, spinnerOrdem;
    private CheckBox checkFiltroPago;
    private ListView listView;

    // Dados
    private DespesaDatabase db;
    private DespesaAdapter adapter;
    private int idEditando = -1; // -1 = novo filme

    private final String[] CATEGORIA = {"Lazer", "Mercado", "Educação",
            "FastFood", "Transporte"};
    private final String[] DESPESA_CATEGORIA; // "Todos" + CATEORIA
    private final String[] ORDENS = {"Descrição", "Valor", "Data"};

    private final String[] FORMA_PAGAMENTO = {"Pix", "Debito", "Crédito",
            "Boleto", "Pay Pal", "Vale"};
    private final String[] DESPESA_PAGAMENTO; // "Todos" + FORMA_PAGAMENTO

    public MainActivity() {
        DESPESA_CATEGORIA = new String[CATEGORIA.length + 1];
        DESPESA_CATEGORIA[0] = "Todos";
        System.arraycopy(CATEGORIA, 0,DESPESA_CATEGORIA, 1, CATEGORIA.length);

        DESPESA_PAGAMENTO = new String[FORMA_PAGAMENTO.length +1];
        DESPESA_PAGAMENTO[0] = "Todos";
        System.arraycopy(FORMA_PAGAMENTO, 0,DESPESA_PAGAMENTO, 1, FORMA_PAGAMENTO.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DespesaDatabase(this);
        vincularViews();
        configurarSpinners();
        configurarBotaoSalvar();
        configurarLista();
        atualizarLista();
    }
    private void vincularViews() {
        etDescricao         = findViewById(R.id.etDescricao);
        etValor       = findViewById(R.id.etValor);
        etData            = findViewById(R.id.etData);
        spinnerCategoria    = findViewById(R.id.spinnerCategoria);
        spinnerFormaPagamento = findViewById(R.id.spinnerFormaPagamento);
        checkFoiPago      = findViewById(R.id.checkFoiPago);
        btnSalvar        = findViewById(R.id.btnSalvar);
        spinnerFiltro    = findViewById(R.id.spinnerFiltro);
        spinnerOrdem     = findViewById(R.id.spinnerOrdem);
        checkFiltroPago  = findViewById(R.id.checkFiltroPago);
        listView         = findViewById(R.id.listViewDespesas);
    }

    private void configurarSpinners() {
        spinnerCategoria.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, CATEGORIA));

        spinnerFiltro.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, DESPESA_CATEGORIA));

        spinnerFiltro.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, FORMA_PAGAMENTO));

        spinnerOrdem.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ORDENS));

        // Recarrega lista ao mudar filtro ou ordem
        AdapterView.OnItemSelectedListener reload = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long l) { atualizarLista(); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerFiltro.setOnItemSelectedListener(reload);
        spinnerOrdem.setOnItemSelectedListener(reload);
        checkFiltroPago.setOnCheckedChangeListener((b, c) -> atualizarLista());
    }
    private void configurarBotaoSalvar() {
        btnSalvar.setOnClickListener(v -> {
            String descricao = etDescricao.getText().toString().trim();
            if (descricao.isEmpty()) {
                Toast.makeText(this, "Descrição obrigatória", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoria = spinnerCategoria.getSelectedItem().toString();
            Float valor  = Float.parseFloat(etValor.getText().toString());
            String data        = etData.getText().toString().trim();
            String pagamento  = spinnerFormaPagamento.getSelectedItem().toString();
            boolean foiPago = checkFoiPago.isChecked();

            if (idEditando == -1) {
                // NOVO
                db.inserir(new Despesa(descricao, categoria, 0, data, pagamento, foiPago));
                Toast.makeText(this, "Despesa salvo!", Toast.LENGTH_SHORT).show();
            } else {
                // EDIÇÃO
                db.atualizar(new Despesa(idEditando, descricao, categoria, 0, data, pagamento, foiPago));
                Toast.makeText(this, "Despesa atualizada!", Toast.LENGTH_SHORT).show();
                idEditando = -1;
                btnSalvar.setText("Salvar");
            }

            limparFormulario();
            atualizarLista();
        });
    }

    private void configurarLista() {
        adapter = new DespesaAdapter(this, db.listar(null, false, null));
        listView.setAdapter(adapter);

        // Clique curto — carrega para edição
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Despesa d = adapter.getItem(position);
            idEditando = d.getId();

            etDescricao.setText(d.getDescricao());
            spinnerCategoria.setSelection(0);
            etValor.setText(String.valueOf(d.getValor()));
            spinnerFormaPagamento.setSelection(0);
            checkFoiPago.setChecked(d.isFoiPaga());
            btnSalvar.setText("Atualizar");

            // Seleciona o gênero correto no spinner
            for (int i = 0; i < CATEGORIA.length; i++) {
                if (CATEGORIA[i].equals(d.getCategoria())) {
                    spinnerCategoria.setSelection(i);
                    break;
                }
            }
            // Seleciona o gênero correto no spinner
            for (int i = 0; i < FORMA_PAGAMENTO.length; i++) {
                if (FORMA_PAGAMENTO[i].equals(d.getFormaPagamento())) {
                    spinnerFormaPagamento.setSelection(i);
                    break;
                }
            }
        });


        // Clique longo — confirma exclusão
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Despesa d = adapter.getItem(position);
            new AlertDialog.Builder(this)
                    .setTitle("Excluir")
                    .setMessage("Deseja excluir \"" + d.getDescricao() + "\"?")
                    .setPositiveButton("Excluir", (dialogInterface, w) -> {
                        db.deletar(d.getId());
                        atualizarLista();
                        Toast.makeText(this, "Excluído", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
    }

    private void atualizarLista() {
        String categoriaFiltro = spinnerFiltro.getSelectedItem().toString();
        boolean pago      = checkFiltroPago.isChecked();
        String ordemStr     = spinnerOrdem.getSelectedItem().toString();

        String ordem = null;
        if ("Valor".equals(ordemStr)) ordem = "valor";
        if ("Data".equals(ordemStr))  ordem = "Data";

        adapter.atualizar(db.listar(categoriaFiltro, pago, ordem));
    }
    private void limparFormulario() {
        etDescricao.setText("");
        spinnerCategoria.setSelection(0);
        etValor.setText("");
        etData.setText("");
        spinnerFormaPagamento.setSelection(0);
        checkFoiPago.setChecked(false);
    }

}
