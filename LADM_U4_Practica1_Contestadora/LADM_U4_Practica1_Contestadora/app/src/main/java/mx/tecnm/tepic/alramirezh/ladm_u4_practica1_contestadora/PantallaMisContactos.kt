package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pantalla_mis_contactos.*
import java.lang.Exception

class PantallaMisContactos : AppCompatActivity() {

    val baseDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_mis_contactos)

        var listaContactos = cargarListaContactos()
        var total = listaContactos.size
        var con = 1
        listaContactos.forEach{
            if(con<=total/2){
                it.tipoLista = true
            }else{
                it.tipoLista = false
            }
            con+=1
            addContacto(it)
        }
        AlertDialog.Builder(this).setTitle("Mensaje")
            .setMessage("A CONTINUACION SE CONSULTARÁ TU LISTA DE CONTACTOS Y SE MOSTRARÁ EN ESTA PANTALLA.\n\n" +
                    "POR DEFAULT SE CARGARÁ LA MITAD EN LA LISTAD BLANCA Y LA OTRA EN LA LISTA NEGRA.\n\n" +
                    "SI DESEAS CAMBIAR UN CONTACTO A LA OTRA LISTA DA CLIC EN EL")
            .setPositiveButton("OK"){ view , d ->
                view.dismiss()
            }.show()
        botonSubir.setOnClickListener {
            if(listaContactos.size!=0) subirContactos(listaContactos)
            else ToastPersonalizado(this,"NO HAY CONTACTOS PARA SUBIR",true).show()
        }
        botonRegresar.setOnClickListener {
            finish()
        }
    }


    fun cargarListaContactos():ArrayList<Contacto> {
        var resultado = ArrayList<Contacto>()
        try {
            val cursorContactos = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

            if (cursorContactos!!.moveToFirst()) {
                do {
                    var columna = cursorContactos.getColumnIndex(ContactsContract.Contacts._ID)
                    var idContacto = cursorContactos!!.getInt(columna)

                    var columna2 = cursorContactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    var nombreContacto = cursorContactos.getString(columna2)

                    var columna3 = cursorContactos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                    if (cursorContactos.getInt(columna3) > 0) {
                        var cursorCel = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(idContacto.toString()), null
                        )

                        var columna4 = cursorCel!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        var listaNum = ArrayList<String>()
                        while (cursorCel!!.moveToNext()) {
                            var num = cursorCel.getString(columna4).replace(" ","").replace("-","")
                            var encontrado = false
                            listaNum.forEach{
                                if(it==num) {
                                    encontrado = true
                                }
                            }
                            if(encontrado==false){ listaNum.add(num) }

                        }
                        listaNum.forEach{
                            resultado.add(Contacto(nombreContacto,it,true))
                        }
                        cursorCel.close()


                    }


                } while (cursorContactos.moveToNext())

                return resultado

            }
        }catch (e: Exception){
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()

        }
        return resultado
    }

    fun addContacto( contacto: Contacto ) {
        // LINEAR LAYOUT PRINCIPAL
        val contenedorMain = LinearLayout(this)
        contenedorContactos.addView(contenedorMain)

        val p1 = contenedorMain.layoutParams as LinearLayout.LayoutParams
        p1.width = LinearLayout.LayoutParams.MATCH_PARENT
        p1.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p1.bottomMargin = 12
        contenedorMain.layoutParams = p1

        contenedorMain.orientation = LinearLayout.VERTICAL
        contenedorMain.setPadding(3, 4, 0, 4)

        if (contacto.tipoLista == true) {
            //TRUE = LISTA BLANCA
            contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))

        } else {
            //FALSO = LISTA NEGRA

            contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
        }


         //=================================================
        // LINEAR LAYOUT NOMBRE
        //=================================================
        val contenedorNombre = LinearLayout(this)
        contenedorMain.addView(contenedorNombre)

        val p2 = contenedorNombre.layoutParams as LinearLayout.LayoutParams
        p2.width = LinearLayout.LayoutParams.MATCH_PARENT
        p2.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p2.bottomMargin = 5
        contenedorNombre.layoutParams = p2
        contenedorNombre.orientation = LinearLayout.HORIZONTAL

        //=================================================
        // IMAGE VIEW PERSONA
        //=================================================
        val imagenPersona = ImageView(this)
        contenedorNombre.addView(imagenPersona)

        val p3 = imagenPersona.layoutParams as LinearLayout.LayoutParams
        p3.width = LinearLayout.LayoutParams.WRAP_CONTENT
        p3.height = LinearLayout.LayoutParams.MATCH_PARENT
        p3.gravity = Gravity.CENTER_VERTICAL
        imagenPersona.layoutParams = p3

        imagenPersona.setImageResource(R.drawable.icono_persona)
        if (contacto.tipoLista == true) {
            imagenPersona.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
            imagenPersona.setColorFilter(
                ContextCompat.getColor(this, R.color.negroLigero),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            imagenPersona.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
            imagenPersona.setColorFilter(
                ContextCompat.getColor(this, R.color.BlancoLigero),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        //=================================================
        // TEXT VIEW ( NOMBRE )
        //=================================================
        val txtNombre = TextView(this)
        contenedorNombre.addView(txtNombre)

        val p4 = txtNombre.layoutParams as LinearLayout.LayoutParams
        p4.width = LinearLayout.LayoutParams.MATCH_PARENT
        p4.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p4.gravity = Gravity.CENTER
        txtNombre.layoutParams = p4

        txtNombre.setPadding(3, 0, 3, 3)
        txtNombre.setText(contacto.Nombre)
        txtNombre.textAlignment = View.TEXT_ALIGNMENT_CENTER
        txtNombre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        txtNombre.setTypeface(Typeface.DEFAULT_BOLD)
        if (contacto.tipoLista == true) {
            txtNombre.setTextColor(ContextCompat.getColor(this, R.color.negroLigero))
        } else {
            txtNombre.setTextColor(ContextCompat.getColor(this, R.color.BlancoLigero))
        }
        ToastPersonalizado(this,contenedorNombre.childCount.toString(),true).show()







         //=================================================
        // LINEAR LAYOUT TELEFONO
        //=================================================
        val contenedorTelefono = LinearLayout(this)
        contenedorMain.addView(contenedorTelefono)

        val p5 = contenedorTelefono.layoutParams as LinearLayout.LayoutParams
        p5.width = LinearLayout.LayoutParams.MATCH_PARENT
        p5.height = LinearLayout.LayoutParams.WRAP_CONTENT

        contenedorTelefono.layoutParams = p5
        contenedorTelefono.orientation = LinearLayout.HORIZONTAL

        //=================================================
        // IMAGE VIEW TELEFONO
        //=================================================
        val imagenTelefono = ImageView(this)
        contenedorTelefono.addView(imagenTelefono)

        val p6 = imagenTelefono.layoutParams as LinearLayout.LayoutParams
        p6.width = LinearLayout.LayoutParams.WRAP_CONTENT
        p6.height = LinearLayout.LayoutParams.MATCH_PARENT
        p6.gravity = Gravity.CENTER_VERTICAL
        imagenTelefono.layoutParams = p6

        imagenTelefono.setImageResource(R.drawable.icono_telefono)
        if (contacto.tipoLista == true) {
            imagenTelefono.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
            imagenTelefono.setColorFilter(
                ContextCompat.getColor(this, R.color.negroLigero),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            imagenTelefono.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
            imagenTelefono.setColorFilter(
                ContextCompat.getColor(this, R.color.BlancoLigero),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        //=================================================
        // TEXT VIEW ( TELEFONO )
        //=================================================
        val txtTelefono = TextView(this)
        contenedorTelefono.addView(txtTelefono)

        val p7 = txtTelefono.layoutParams as LinearLayout.LayoutParams
        p7.width = LinearLayout.LayoutParams.MATCH_PARENT
        p7.height = LinearLayout.LayoutParams.WRAP_CONTENT
        p7.gravity = Gravity.CENTER
        txtTelefono.layoutParams = p7
        txtTelefono.textAlignment = View.TEXT_ALIGNMENT_CENTER
        txtTelefono.setPadding(3, 0, 3, 3)
        txtTelefono.setText(contacto.Telefono)
        txtTelefono.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        txtTelefono.setTypeface(Typeface.DEFAULT_BOLD)
        if (contacto.tipoLista == true) {
            txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.white))
        }



        contenedorMain.setOnClickListener {
            if (contacto.tipoLista == true) {
                //TRUE = LISTA BLANCA
                contacto.tipoLista = false
                contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenPersona.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenPersona.setColorFilter(
                    ContextCompat.getColor(this, R.color.BlancoLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                txtNombre.setTextColor(ContextCompat.getColor(this, R.color.BlancoLigero))
                imagenTelefono.setBackgroundColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenTelefono.setColorFilter(
                    ContextCompat.getColor(this, R.color.BlancoLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.BlancoLigero))
            } else {
                //FALSO = LISTA NEGRA
                contacto.tipoLista = true
                contenedorMain.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))

                imagenPersona.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
                imagenPersona.setColorFilter(
                    ContextCompat.getColor(this, R.color.negroLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                txtNombre.setTextColor(ContextCompat.getColor(this, R.color.negroLigero))
                imagenTelefono.setBackgroundColor(ContextCompat.getColor(this, R.color.BlancoLigero))
                imagenTelefono.setColorFilter(
                    ContextCompat.getColor(this, R.color.negroLigero),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                txtTelefono.setTextColor(ContextCompat.getColor(this, R.color.negroLigero))
            }
        }



    }










    fun subirContactos(Lista:ArrayList<Contacto>){
            //BORRAR TODOS LOS CONTACTOS DE LA BD   Y SUBIR LOS NUEVOS
            ToastPersonalizado(this,"Subiendo contactos a la Base de Datos \n ESTO TARDARA UN MOMENTO, POR FAVOR ESPERE",true).show()
            var bandera = true
            baseDatos.collection("contactos").get().addOnSuccessListener { documentos ->
                if(bandera) {
                    bandera = false
                    var i = documentos.size()
                    var c = 0
                    if (i == 0) {
                        insertarContacto(Lista)

                    } else {
                        for (doc in documentos!!) {
                            baseDatos.collection("contactos").document(doc.id).delete()
                                .addOnSuccessListener {
                                    c += 1
                                    if (c == i) {
                                        insertarContacto(Lista)
                                    }

                                }
                                .addOnFailureListener {
                                    ToastPersonalizado(this, "Fallo al eliminar un contacto",true).show()
                                }
                        }
                    }
                }
            }
                .addOnFailureListener {
                    ToastPersonalizado(this,"Fallido",true).show()
                }
             // BOORRAR LLAMADAS PERDIDAS

        var bandera2 = true
        baseDatos.collection("llamadasPerdidas").get().addOnSuccessListener { documentos ->
            if(bandera2) {
                bandera2 = false
                var i = documentos.size()
                var c = 0
                for (doc in documentos!!) {
                    baseDatos.collection("llamadasPerdidas").document(doc.id).delete()
                        .addOnSuccessListener {
                            c += 1
                            if (c == i) ToastPersonalizado(this,"SE HAN ELIMINADO TODAS LAS LLAMADAS PERDIDAS REGISTRADAS",false).show()

                        }
                        .addOnFailureListener {
                                ToastPersonalizado(this, "ALGO FALLO AL ELIMINAR 1 LLAMADA PERDIDA",true).show()
                        }
                    }
                }
            }
            .addOnFailureListener {
                ToastPersonalizado(this,"Fallo al acceder a las llamadas perdidas",true).show()
            }
        }










    fun insertarContacto(Lista: ArrayList<Contacto>){
        var c = 0

        Lista.forEach{
            var tipo = ""
            if(it.tipoLista){
                tipo = "Blanca"
            }else{
                tipo = "Negra"
            }
            var datos = hashMapOf("NOMBRE" to it.Nombre, "TELEFONO" to it.Telefono, "TIPOLISTA" to tipo )
            val nota = it
            baseDatos.collection("contactos").add(datos as Any).addOnSuccessListener {
                c++
                if(c==Lista.size) ToastPersonalizado(this,"SE AGREGARON LOS CONTACTOS A LA BASE DE DATOS",true).show()
            }
                .addOnFailureListener {
                    ToastPersonalizado(this, "FALLO AL INSERTAR UN CONTACTO",true).show()
                    //subidos = false
                }

        }
    }

}