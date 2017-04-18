package com.tercalivre.blog.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alan on 15/04/17.
 */

public class RetornaHoraAmigavel {

    //"date":"2017-04-15 23:16:56"
    public Date retornarData(String dataApi){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date data = null;
        try {
            data = myFormat.parse(dataApi);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public String retornaDescricaoDiferenca(String dataApi,Date data){

        Date dataRecebida = retornarData(dataApi);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

        return sdf.format(dataRecebida);

    }

    public String retornaDescricaoDiferenca(String dataApi){
        return retornaDescricaoDiferenca(dataApi,new Date());
    }

    private boolean testarMesmoDia(Date data01, Date data02){
        data01 = retirarHoraData(data01);
        data02 = retirarHoraData(data02);


        return data01.compareTo(data02) == 0;
    }

    private long minutosEntreDuasDatas(Date data01, Date data02){

        long quantidadeMinutos = Math.abs(data01.getTime() - data02.getTime()) / 60;

        return quantidadeMinutos;
    }

    private Date retirarHoraData(Date dataComHora){
        Date novaData = new Date();

        novaData.setYear(dataComHora.getDay());
        novaData.setMonth(dataComHora.getMonth());
        novaData.setDate(dataComHora.getDate());

        return novaData;

    }


}
