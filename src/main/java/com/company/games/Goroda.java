package com.company.games;

import com.company.interfaces.IGame;
import java.util.HashSet;


public class Goroda implements IGame
{
    private String[] cities = new String[]
            {
            "москва",
            "анадырь",
            "ростов",
            "волгоград",
            "донецк"
            };
    private HashSet<String> usedCities = new HashSet<>();
    private String currentCity;
    private String err;
    private String lastLetter = "м";
    private Boolean finished = false;

    public String start()
    {
        return "Начнем играть в города!\n" +
                "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Чтобы закончить, введи: хватит\n"+
                "Амстердам";

    }

    public String getName()
    {
        return "Города";
    }

    @Override
    public void readMessage(String str)
    {
        str = str.toLowerCase();

        if (!str.substring(0,1).equals(lastLetter))
        {
            err = "Нужен город на букву " + lastLetter;
            return;
        }

        if (!inBase(str))
        {
            err = "Я не знаю такого города";
            return;
        }
        if (usedCities.contains(str))
        {
            err = "Этот город уже был";
            return;
        }
        updateCurrentCity(str);
        err = null;
    }

    @Override
    public String getMessage() {
        if (err != null)
        {
            return err;
        }

        String city = find(lastLetter);
        if (city != null)
        {
            updateCurrentCity(city);
            return city;
        }
        finished = true;
        return "Я не знаю подходящего города, игра окончена!";
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

    @Override
    public String getQuest()
    {
        /*if (passQuest)
        {
            if (finished)
            {
                return null;
            }
            return getMessage();
        }*/
        if (currentCity == null)
        {
            return null;
        }
        else
        {
            return currentCity;
        }
    }

    private boolean inBase(String value)
    {
        for (String s : cities)
        {
            if (s.equals(value))
            {
                return true;
            }
        }
        return false;
    }

    private String find(String value)
    {
        for (String city : cities)
        {
            if (city.startsWith(value) && !usedCities.contains(city))
            {
                return city;
            }
        }
        return null;
    }

    private void updateCurrentCity(String city)
    {
        usedCities.add(city);
        currentCity = city;
        int lastIndex = currentCity.length();
        lastLetter = currentCity.substring(lastIndex - 1);
        if (lastLetter.equals("ь") || lastLetter.equals("ы"))
        {
            lastLetter = currentCity.substring(lastIndex-2, lastIndex - 1);
        }
    }
}