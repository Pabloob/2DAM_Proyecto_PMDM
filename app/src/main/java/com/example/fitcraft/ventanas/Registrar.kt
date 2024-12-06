package com.example.fitcraft.ventanas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.Boton
import com.example.fitcraft.CampoTexto
import com.example.fitcraft.CampoTextoConContrasena
import com.example.fitcraft.DividerConEspaciado
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.R
import com.example.fitcraft.TextoCentrado
import com.example.fitcraft.TextoInteractivo
import com.example.fitcraft.clases.Usuario
import com.example.fitcraft.conexiones.ConexionPersona
import com.example.fitcraft.ui.theme.*
import java.util.Calendar

class VentanaSecundaria : ComponentActivity() {
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
internal fun Registrar(navController: NavController) {
    val conexionPersona = ConexionPersona() // Instancia para manejar usuarios

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("Seleccionar fecha") }
    var auxAltura by remember { mutableStateOf("") }
    var auxPeso by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") } // Mensaje de error

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            fechaNacimiento = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(50.dp))
                .background(ColorFondoSecundario)
                .padding(30.dp)
        ) {
            TextoCentrado(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = ColorTitulo
            )

            // Nombre y Apellidos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoTexto(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = "Nombre",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                CampoTexto(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    placeholder = "Apellidos",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.info_datos_usuario),
                    contentDescription = "imagen nombre y apellidos",
                    modifier = Modifier.size(50.dp)
                )
            }

            DividerConEspaciado()

            // Fecha de nacimiento
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fechaNacimiento,
                    color = ColorTexto,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { datePickerDialog.show() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.info_fecha_nacimiento),
                    contentDescription = "imagen nombre y apellidos",
                    modifier = Modifier.size(50.dp)
                )
            }

            DividerConEspaciado()

            // Altura y Peso
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoTexto(
                    value = auxAltura,
                    onValueChange = { auxAltura = it },
                    placeholder = "Altura",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.info_altura),
                    contentDescription = "imagen nombre y apellidos",
                    modifier = Modifier.size(50.dp)
                )
                CampoTexto(
                    value = auxPeso,
                    onValueChange = { auxPeso = it },
                    placeholder = "Peso",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.info_peso),
                    contentDescription = "imagen nombre y apellidos",
                    modifier = Modifier.size(50.dp)
                )
            }

            DividerConEspaciado()

            // Nombre de usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CampoTexto(
                    value = nombreUsuario,
                    onValueChange = { nombreUsuario = it },
                    placeholder = "Nombre de usuario",
                    leadingIcon = R.drawable.info_nombre_usuario,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            DividerConEspaciado()

            // Contraseña
            CampoTextoConContrasena(
                value = contrasena,
                onValueChange = { contrasena = it },
                placeholder = "Contraseña",
                leadingIcon = R.drawable.info_contrasena
            )

            DividerConEspaciado()

            // Botón para crear cuenta
            Boton(
                text = "Crear cuenta",
                onClick = {
                    val altura = auxAltura.toFloatOrNull() ?: 0f
                    val peso = auxPeso.toFloatOrNull() ?: 0f

                    if (validarEntrada(
                            nombre,
                            apellidos,
                            fechaNacimiento,
                            altura,
                            peso,
                            nombreUsuario,
                            contrasena
                        )
                    ) {
                        val nuevoUsuario = Usuario(
                            nombre = nombre,
                            apellidos = apellidos,
                            fechaNacimiento = fechaNacimiento,
                            altura = altura.toDouble(),
                            peso = peso.toDouble(),
                            nombreUsuario = nombreUsuario,
                            contrasena = contrasena
                        )
                        // Guardar el usuario con ConexionPersona
                        conexionPersona.agregarUsuario(nuevoUsuario) { exito ->
                            if (exito) {
                                navController.navigate("VentanaIniciarSesion")
                            } else {
                                errorMensaje = "Error al crear el usuario. Inténtalo de nuevo."
                            }
                        }
                    } else {
                        errorMensaje = "Por favor, llena todos los campos correctamente."
                    }
                }
            )

            // Mostrar error si hay
            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextoInteractivo(
                texto = "Iniciar sesión",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.popBackStack() }
            )
        }
    }
}


fun validarEntrada(
    nombre: String,
    apellidos: String,
    fechaNacimiento: String,
    altura: Float,
    peso: Float,
    nombreUsuario: String,
    contrasena: String
): Boolean {
    return nombre.isNotEmpty() && apellidos.isNotEmpty() &&
            fechaNacimiento.isNotEmpty() && altura > 0 &&
            peso > 0 && nombreUsuario.isNotEmpty() &&
            contrasena.isNotEmpty()
}

@Preview
@Composable
private fun Prev() {
    val navController = rememberNavController()
    Registrar(navController)
}
