package com.company;

import com.company.games.Goroda;
import com.company.games.MathGame;
import com.company.interfaces.IGame;


public class Bot
{
    private IGame game;
    private IGame[] games = new IGame[]{new Goroda(), new MathGame()};
    private MemoryGame memory = new MemoryGame();
    private String answer;
    private boolean sendWelcomeMsg = false;
    private Integer numberGame;

    public void sendWelcomeMsg()
    {
        StringBuilder output = new StringBuilder();
        output.append("Выберите игру:\n");
        for (int i=0; i<games.length; i++)
        {
            output.append(games[i].getName() + "-" + i + "\n");
        }
        answer = output.toString();
        sendWelcomeMsg = true;
    }

    public String getAnswer()
    {
        return answer;
    }



    private boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


    private Integer getGameNumber(String userChoice)
    {
        if (!(tryParseInt(userChoice) && (Integer.parseInt(userChoice) < games.length)))
        {
            answer = "Введите корректное значение";
            return null;
        }
        return Integer.parseInt(userChoice);
    }

    public void communicate(String msg)
    {
        String text = msg.toLowerCase();

        if (sendWelcomeMsg) //after sending welcome msg
        {
            numberGame = getGameNumber(text);
            if (numberGame != null)
            {
                game = games[numberGame];
                startPlay();
            }
            sendWelcomeMsg = false;
            return;
        }

        if ("/start".equals(text))
        {
            sendWelcomeMsg();
            return;
        }

        if ("новая".equals(text) || "сохранить".equals(text))
        {
            memory.saveLastGame(game);
            game = null;
            sendWelcomeMsg();
            answer = "игра сохранена! \n" + answer;
            return;
        }
        if ("последняя".equals(text))
        {
            IGame gameNow = memory.getLastGame();
            memory.saveLastGame(game);
            game = gameNow;
            return;
        }
        if ("хватит".equals(text))
        {
            answer = "Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.";
            return;
        }

        if ("не сохранять".equals(text))
        {
            game = null;
            sendWelcomeMsg();
            return;
        }

        progressInput(text);
    }

    private void startPlay()
    {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        output.append(game.start());
        answer = output.toString();
    }

    private void progressInput(String text)
    {
        game.readMessage(text);
        String mess = game.getMessage();
        if (mess == null)
        {
            answer = "Не знаю... Попробуй написать что-то еще";
            return;
        }
        if (game.isFinished())
        {
            finish();
            return;
        }
        answer = mess;
    }

    private void finish()
    {
        if (game instanceof Goroda)
        {
            games[numberGame] = new Goroda();
        }
        else
        {
            games[numberGame] = new MathGame();
        }
        sendWelcomeMsg();
        answer = "Игра окончена \n" + answer;
        game = null;
        return;
    }
}