# 🏋️‍♂️ FitCraft - Aplicación de Rutinas de Gimnasio

**FitCraft** es una aplicación móvil desarrollada en **Kotlin con Jetpack Compose** que permite a los usuarios crear, gestionar y seguir sus rutinas de gimnasio. Utiliza **Firebase** como backend para el almacenamiento de datos y autenticación de usuarios.

---

## 🚀 Tecnologías Utilizadas

- **Kotlin + Jetpack Compose** → UI declarativa moderna.
- **Firebase** → Autenticación y almacenamiento en tiempo real.
- **Navigation Component** → Manejo de múltiples pantallas.
- **ViewModel** → Gestión de estado y persistencia de datos.
- **LiveData + State Management** → Reactividad en la UI.

---

## 📂 Estructura del Proyecto

```
fitcraft/
│── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/fitcraft/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/       # Modelos de datos (Rutina, Ejercicio, Usuario)
│   │   │   │   │   ├── firebase/    # Conexión con Firebase
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screen/      # Pantallas principales
│   │   │   │   │   ├── components/  # Componentes reutilizables
│   │   │   │   ├── viewmodel/       # Manejo de estado
│   │   │   ├── AndroidManifest.xml  # Configuración de la app
│   ├── build.gradle.kts             # Configuración del proyecto
└── README.md                         # Documentación
```

---

## 🎨 Pantallas Principales

### 1️⃣ **Pantalla de Inicio de Sesión**
- Autenticación con **Firebase Authentication**.
- Opción de registro para nuevos usuarios.

### 2️⃣ **Pantalla Principal**
- Muestra las rutinas del usuario.
- Navegación hacia otras secciones de la app.

### 3️⃣ **Gestión de Rutinas**
- Crear nuevas rutinas y asignar ejercicios.
- Guardar y modificar entrenamientos en Firebase.

### 4️⃣ **Lista de Ejercicios**
- Muestra ejercicios disponibles con filtros.
- Permite agregar ejercicios personalizados.

### 5️⃣ **Seguimiento de Entrenamiento**
- Registro de series, repeticiones y descanso.
- Guarda automáticamente el progreso en Firebase.

---

## 🔗 Conexión con Firebase

La aplicación usa **Firebase Realtime Database** para almacenar rutinas y ejercicios.

**Ejemplo de estructura en Firebase:**
```json
{
  "usuarios": {
    "uid_123": {
      "nombre": "Pablo",
      "rutinas": {
        "rutina_1": {
          "nombre": "Full Body",
          "ejercicios": ["Sentadillas", "Press de banca"]
        }
      }
    }
  }
}
```

**Ejemplo de lectura de datos en Kotlin:**
```kotlin
val database = FirebaseDatabase.getInstance().getReference("usuarios")
database.child(uid).child("rutinas").get().addOnSuccessListener { dataSnapshot ->
    val rutinas = dataSnapshot.children.map { it.getValue(Rutina::class.java) }
    viewModel.actualizarRutinas(rutinas)
}
```

---

## 📱 Diseño Responsivo con Jetpack Compose

FitCraft se adapta automáticamente a distintos tamaños de pantalla gracias a **Jetpack Compose**.

Ejemplo de **Uso de Media Queries**:
```kotlin
@Composable
fun ResponsiveLayout() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    if (screenWidth < 600) {
        Column { /* Móvil */ }
    } else {
        Row { /* Tablet */ }
    }
}
```

---

## 🔧 Instalación y Ejecución

1️⃣ Clona el repositorio:
```sh
git clone https://github.com/Pabloob/FitCraft.git
```

2️⃣ Abre el proyecto en **Android Studio**.

3️⃣ Conéctalo con Firebase en `Tools > Firebase`.

4️⃣ Ejecuta la app en un emulador o dispositivo físico.

```sh
gradlew assembleDebug
```

---

## 📢 Despliegue

Para generar un APK de producción:
```sh
gradlew assembleRelease
```

Si deseas subirlo a la Play Store, sigue las guías de **Google Play Console**.

---

## 📌 Autor

**Pablo Orbea Benitez** – [GitHub](https://github.com/Pabloob) | [LinkedIn](https://www.linkedin.com/in/pabloob5)

🏋️‍♂️ ¡Entrena de manera eficiente con FitCraft! 🚀
