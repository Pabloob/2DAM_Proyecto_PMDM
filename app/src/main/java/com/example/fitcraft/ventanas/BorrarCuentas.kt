package com.example.fitcraft.ventanas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.clases.Usuario
import com.example.fitcraft.conexiones.ConexionPersona
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.PanelNavegacionInferior
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.FitCraftTheme

class VentanaBorrarCuentas : ComponentActivity() {
    private val datosPersistentes: DatosPersistentes by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navHostController = navController,
                    datosPersistentes = datosPersistentes
                )
            }
        }
    }
}

@Composable
internal fun Borrar(navController: NavController) {
    val conexionPersona = ConexionPersona()
    val usuarios = remember { mutableStateListOf<Usuario>() }
    val context = LocalContext.current

    // Cargar usuarios desde Firebase al iniciar el componente
    LaunchedEffect(Unit) {
        conexionPersona.obtenerTodosLosUsuarios { listaUsuarios ->
            usuarios.clear()
            usuarios.addAll(listaUsuarios)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.TopCenter)
        ) {
            // Encabezado
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = ColorFondoSecundario)
            ) {
                Text(
                    text = "Cuentas",
                    color = ColorTexto,
                    lineHeight = 2.5.em,
                    style = TextStyle(
                        fontSize = 30.sp, fontWeight = FontWeight.Bold
                    )
                )
            }

            // Lista de usuarios
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = ColorFondoSecundario)
                    .padding(20.dp)
            ) {
                if (usuarios.isEmpty()) {
                    Text(
                        text = "No hay usuarios disponibles",
                        color = ColorError,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    usuarios.forEach { usuario ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = usuario.nombreUsuario,
                                color = ColorError,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.clickable {
                                    conexionPersona.borrarUsuarioPorNombre(usuario.nombreUsuario) { exito ->
                                        if (exito) {
                                            usuarios.remove(usuario)
                                            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Navegación inferior
        PanelNavegacionInferior(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun AndroidMedium1Preview() {
    val navController = rememberNavController()
    Borrar(navController = navController)
}