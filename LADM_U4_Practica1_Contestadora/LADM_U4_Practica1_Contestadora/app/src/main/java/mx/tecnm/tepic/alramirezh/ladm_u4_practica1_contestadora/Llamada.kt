package mx.tecnm.tepic.alramirezh.ladm_u4_practica1_contestadora

import java.util.*

class Llamada(Telefono:String, Fecha:Calendar) {
    var Telefono = Telefono
    var Fecha = getFecha(Fecha)
    var contacto:Contacto? = null


    fun getFecha(Fecha:Calendar) : String {
        return getDia(Fecha) + " / " + getNombreMes(Fecha) + " / " + getYear(Fecha) + " " + getHora(Fecha) + ":" + getMinutos(Fecha) + ":" + getSegundos(Fecha)
    }

    fun getDia(Fecha:Calendar): String {
        var dia = Fecha.get( Calendar.DAY_OF_MONTH ).toString()
        if( dia.toInt() < 10){ dia = "0$dia" }
        return dia
    }

    fun getMes(Fecha:Calendar): String {
        var mes = (Fecha.get( Calendar.MONTH ) + 1).toString()
        if( mes.toInt() < 10){ mes = "0$mes" }
        return mes
    }

    fun getNombreMes(Fecha:Calendar): String {
        var mes = ""
        when( Fecha.get( Calendar.MONTH ) + 1 )
        {
            1 -> { mes = "Enero"}
            2 -> { mes = "Febrero"}
            3 -> { mes = "Marzo"}
            4 -> { mes = "Abril"}
            5 -> { mes = "Mayo"}
            6 -> { mes = "Junio"}
            7 -> { mes = "Julio"}
            8 -> { mes = "Agosto"}
            9 -> { mes = "Septiembre"}
            10 -> { mes = "Octubre" }
            11 -> { mes = "Noviembre"}
            12 -> {mes = "Diciembre"}
        }
        return mes
    }


    fun getYear(Fecha:Calendar): String {
        return Fecha.get( Calendar.YEAR ).toString()
    }



    fun getHora(Fecha:Calendar): String {
        var hora = (Fecha.get( Calendar.HOUR_OF_DAY )).toString()
        if( hora.toInt() < 10){ hora = "0$hora" }
        return hora
    }

    fun getMinutos(Fecha:Calendar): String {
        var min = (Fecha.get( Calendar.MINUTE )).toString()
        if( min.toInt() < 10){ min = "0$min" }
        return min
    }

    fun getSegundos(Fecha:Calendar): String {
        var seg = (Fecha.get( Calendar.SECOND )).toString()
        if( seg.toInt() < 10){ seg = "0$seg" }
        return seg
    }
}