package com.tercalivre.blog;


import com.tercalivre.blog.utils.RetornaHoraAmigavel;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RetornaHoraAmigavelTest {

    @Test
    public void retornaObjetoDateCorreto(){

        //"date":"2017-04-15 23:16:56"
        RetornaHoraAmigavel rha = new RetornaHoraAmigavel();
        Date date = rha.retornarData("2017-04-15 23:16:56");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        Assert.assertEquals(2017,calendar.get(Calendar.YEAR));
        Assert.assertEquals(04,calendar.get(Calendar.MONTH)+1);
        Assert.assertEquals(15,calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(23,calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(16,calendar.get(Calendar.MINUTE));
        Assert.assertEquals(56,calendar.get(Calendar.SECOND));

    }

    //@Test
    //public void retornaDescricaoCertaPara5Minutos(){
//        RetornaHoraAmigavel rha = new RetornaHoraAmigavel();
//        String descricao = rha.retornaDescricaoDiferenca(String dataApi,Date data);
//    }

}
