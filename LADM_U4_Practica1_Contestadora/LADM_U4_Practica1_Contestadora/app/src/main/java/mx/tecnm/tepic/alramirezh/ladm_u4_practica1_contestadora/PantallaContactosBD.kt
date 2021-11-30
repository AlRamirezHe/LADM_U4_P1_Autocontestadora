package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pantalla_mis_contactos.*

class PantallaContactosBD : AppCompatActivity() {

    val baseDatos = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_contactos_bd)
        MostrarContactos()
    }

    fun MostrarContactos() {


        baseDatos.collection("contactos").get().addOnSuccessListener { documentos ->

            var i = documentos.size()
            var c = 0
            for (doc in documentos!!) {
                var tipo = true
                if(doc.getString("TIPOLISTA")=="Blanca"){
                    tipo = true
                }else{
                    tipo = false
                }
                var nombre =doc.getString("NOMBRE").toString()
                var telefono = doc.getString("TELEFONO").toString()
                addContacto(Contacto(nombre,telefono,tipo))
            }
        }.addOnFailureListener {
                ToastPersonalizado(this, "Fallido", true).show()
            }
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

    }
}