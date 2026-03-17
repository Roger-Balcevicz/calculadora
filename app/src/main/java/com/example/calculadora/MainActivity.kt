package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF101010)
                ) {
                    CalculadoraScreen()
                }
            }
        }
    }
}

@Composable
fun CalculadoraScreen() {
    var display by remember { mutableStateOf("0") }
    var numero1 by remember { mutableStateOf<Double?>(null) }
    var operacao by remember { mutableStateOf<String?>(null) }
    var limparDisplay by remember { mutableStateOf(false) }

    fun aoClicarNumero(valor: String) {
        display = if (display == "0" || limparDisplay) {
            limparDisplay = false
            valor
        } else {
            display + valor
        }
    }

    fun aoClicarOperacao(op: String) {
        numero1 = display.toDoubleOrNull()
        operacao = op
        limparDisplay = true
    }

    fun calcularResultado() {
        val numero2 = display.toDoubleOrNull()
        val n1 = numero1
        val op = operacao

        if (n1 != null && numero2 != null && op != null) {
            if (op == "/" && numero2 == 0.0) {
                display = "Erro"
            } else {
                val resultado: Double = when (op) {
                    "+" -> n1 + numero2
                    "-" -> n1 - numero2
                    "*" -> n1 * numero2
                    "/" -> n1 / numero2
                    else -> 0.0
                }

                display = if (resultado % 1.0 == 0.0) {
                    resultado.toInt().toString()
                } else {
                    resultado.toString()
                }
            }

            numero1 = null
            operacao = null
            limparDisplay = true
        }
    }

    fun limparTudo() {
        display = "0"
        numero1 = null
        operacao = null
        limparDisplay = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = display,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.Bottom)
                .padding(16.dp)
        )

        val botoes = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+"),
            listOf("C")
        )

        botoes.forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                linha.forEach { botao ->
                    Button(
                        onClick = {
                            when (botao) {
                                in listOf("0","1","2","3","4","5","6","7","8","9") -> aoClicarNumero(botao)
                                "." -> {
                                    if (!display.contains(".")) {
                                        aoClicarNumero(".")
                                    }
                                }
                                "+", "-", "*", "/" -> aoClicarOperacao(botao)
                                "=" -> calcularResultado()
                                "C" -> limparTudo()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (botao) {
                                in listOf("+", "-", "*", "/", "=") -> Color(0xFFFF9800)
                                "C" -> Color(0xFFE53935)
                                else -> Color(0xFF2C2C2C)
                            }
                        )
                    ) {
                        Text(
                            text = botao,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                }

                if (linha.size == 1) {
                    Spacer(modifier = Modifier.weight(3f))
                }
            }
        }
    }
}