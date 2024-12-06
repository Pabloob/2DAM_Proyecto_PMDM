package com.example.fitcraft.ventanas


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.Boton
import com.example.fitcraft.CampoTexto
import com.example.fitcraft.CampoTextoConContrasena
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.R
import com.example.fitcraft.TextoCentrado
import com.example.fitcraft.TextoInteractivo
import com.example.fitcraft.clases.Ejercicio
import com.example.fitcraft.clases.Usuario
import com.example.fitcraft.conexiones.ConexionPersona
import com.example.fitcraft.ui.theme.*

class VentanaPrincipal : ComponentActivity() {
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

class DatosPersistentes : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null) // Puede ser null inicialmente
    var diaRutina = mutableStateListOf<String>()
    val seriesYRepeticiones =mutableStateMapOf<Ejercicio, Pair<String, String>>() // Series y repeticiones
    val ejerciciosSeleccionados = mutableStateListOf<Ejercicio>()
}

@Composable
fun IniciarSesion(navController: NavController, datosPersistentes: DatosPersistentes) {
    // Estados del formulario
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    //Crear conexion
    val conexion = ConexionPersona()

    // Función para validar credenciales
    fun validarCredenciales(nombre:String) {
        conexion.obtenerUsuarioPorNombre(nombre) { usuario ->
            if (usuario != null && usuario.contrasena == contrasena) {
                // Usuario válido: actualizar el ViewModel y navegar
                datosPersistentes.usuarioActual = usuario
                navController.navigate("VentanaInicio")
            } else {
                // Mostrar error si las credenciales son incorrectas o no existe el usuario
                errorMensaje = "Usuario o contraseña incorrectos"
            }
        }
    }

    // Diseño principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(esquina50)
                .background(ColorFondoSecundario)
                .padding(30.dp)
        ) {
            TextoCentrado(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 75.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier,
                color = ColorTitulo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre de Usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CampoTexto(
                    value = nombreUsuario,
                    onValueChange = {
                        nombreUsuario = it
                        errorMensaje = ""
                    },
                    placeholder = "Nombre de usuario",
                    leadingIcon = R.drawable.info_nombre_usuario,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Contraseña
            CampoTextoConContrasena(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorMensaje = ""
                },
                placeholder = "Contraseña",
                leadingIcon = R.drawable.info_contrasena
            )

            // Mensaje de error
            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Iniciar Sesión
            Boton(
                text = "Iniciar sesión",
                onClick = {
                    validarCredenciales(nombreUsuario)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto interactivo: ¿Olvidaste tu contraseña?
            TextoInteractivo(
                texto = "¿Olvidaste la contraseña?",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /* Manejar evento de recuperación */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto interactivo: Crear cuenta
            TextoInteractivo(
                texto = "Crear cuenta",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.navigate("VentanaRegistrar") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AndroidMedium1Preview() {
    val navController = rememberNavController()
    val datosPersistentes = DatosPersistentes().apply {
        // Proporcionar datos ficticios al UsuarioViewModel para el Preview
        usuarioActual = Usuario(
            nombreUsuario = "usuario_demo",
            contrasena = "1234"
        )
    }

    // Evitar operaciones en Firebase en el Preview
    IniciarSesion(
        navController = navController,
        datosPersistentes = datosPersistentes
    )
}
