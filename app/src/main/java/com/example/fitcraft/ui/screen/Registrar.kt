package com.example.fitcraft.ui.screen

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.fitcraft.R
import com.example.fitcraft.data.firebase.ConexionPersona
import com.example.fitcraft.data.model.Usuario
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.CampoTexto
import com.example.fitcraft.ui.components.CampoTextoConContrasena
import com.example.fitcraft.ui.components.DividerConEspaciado
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.components.TextoInteractivo
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.ui.theme.esquina25
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado
import java.util.Calendar

class VentanaSecundaria : ComponentActivity() {
    private val usuarioLogeado: UsuarioLogeado by viewModels()
    private val datosRutina: DatosRutina by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navHostController = navController,
                    usuarioLogeado = usuarioLogeado,
                    datosRutina,
                )
            }
        }
    }
}

@Composable
internal fun Registrar(navController: NavController) {
    val conexionPersona = ConexionPersona()

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var auxAltura by remember { mutableStateOf("") }
    var auxPeso by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoTexto(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorMensaje = ""
                    },
                    placeholder = "Nombre",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))
                CampoTexto(
                    value = apellidos,
                    onValueChange = {
                        apellidos = it
                        errorMensaje = ""
                    },
                    placeholder = "Apellidos",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            DividerConEspaciado()

            CampoTexto(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                },
                placeholder = "E-mail",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.width(16.dp))

            DividerConEspaciado()

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = ColorTexto,
                    focusedTextColor = ColorTexto,
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White
                ),
                value = fechaNacimiento,
                onValueChange = {},
                enabled = false,
                placeholder = {
                    Text("Seleccionar fecha", color = ColorTexto)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.DateRange, contentDescription = null
                    )
                },
                shape = esquina25,
                modifier = Modifier
                    .clickable { datePickerDialog.show() }
                    .fillMaxWidth(),
            )

            DividerConEspaciado()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoTexto(
                    value = auxAltura,
                    onValueChange = {
                        auxAltura = it
                        errorMensaje = ""
                    },
                    placeholder = "Altura",
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.info_altura),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                CampoTexto(
                    value = auxPeso,
                    onValueChange = {
                        auxPeso = it
                        errorMensaje = ""
                    },
                    placeholder = "Peso",
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.info_peso),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            DividerConEspaciado()

            CampoTexto(
                value = nombreUsuario,
                onValueChange = {
                    nombreUsuario = it
                    errorMensaje = ""
                },
                placeholder = "Nombre de usuario",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DividerConEspaciado()

            CampoTextoConContrasena(
                value = contrasena,
                onValueChange = { contrasena = it },
                placeholder = "Contraseña",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock, contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            DividerConEspaciado()

            Boton(
                text = "Crear cuenta",
                onClick = {
                    val altura = auxAltura.toFloatOrNull() ?: 0f
                    val peso = auxPeso.toFloatOrNull() ?: 0f

                    if (validarEntrada(
                            nombre,
                            apellidos,
                            fechaNacimiento,
                            email,
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
                            email = email,
                            altura = altura,
                            peso = peso,
                            nombreUsuario = nombreUsuario,
                            contrasena = contrasena
                        )
                        conexionPersona.registrarUsuario(nuevoUsuario) { exitoso, mensaje ->
                            if (exitoso) {
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
    email: String,
    fechaNacimiento: String,
    altura: Float,
    peso: Float,
    nombreUsuario: String,
    contrasena: String
): Boolean {
    return nombre.isNotEmpty() && apellidos.isNotEmpty() &&
            fechaNacimiento.isNotEmpty() && email.isNotEmpty() && altura > 0 &&
            peso > 0 && nombreUsuario.isNotEmpty() &&
            contrasena.isNotEmpty()
}