
#include <iostream>
#include <string>
#include <fstream>
#include <map>
using namespace std;

class Token
{
private:
    string lexem;
    int column;
    int row;
    int token;
    bool eof;
public:
    Token(const string& aLexem, int aColumn, int aRow, int aToken, bool aEof)
        : lexem(aLexem), column(aColumn), row(aRow), token(aToken), eof(aEof)
    {}

    Token() : lexem("") {}

    const string getLexem() const
    {
        return lexem;
    }
    const int getRow() const
    {
        return row;
    }
    const int getColumn() const
    {
        return column;
    }
    const int getToken() const
    {
        return token;
    }
    const bool isEof() const
    {
        return eof;
    }

    friend ostream& operator<<(ostream& out, const Token& aToken) {
        out << "'" << aToken.getLexem() << "' " << aToken.getToken() << " (" << aToken.getRow() << ", " << aToken.getColumn() << ") " << (aToken.isEof() ? "true" : "false");
        return out;
    }
};




class Scanner
{
private:
    istream* input;
    Token lastToken;
    int row;
    int column;
    const static int maxState = 32;
    const static int startState = 0;
    const static int noEdge = -1;
    int automata[maxState + 1][256];
    int finite[maxState + 1];

    void initAutomata()
    {
        for (int i = 0; i <= maxState; i++)
        {
            for (int j = 0; j < 256; j++)
            {
                automata[i][j] = noEdge;
            }

        }

        for (int i = '0'; i <= '9'; i++)
        {
            automata[0][i] = automata[1][i] = 1;

            automata[2][i] = automata[3][i] = 3;

            automata[4][i] = 4;

            automata[11][i] = automata[12][i] = automata[13][i] = automata[14][i] =
            automata[15][i] = automata[16][i] = automata[17][i] = automata[18][i] = 
            automata[19][i] = automata[20][i] = automata[21][i] = automata[22][i] = 
            automata[23][i] = automata[24][i] = automata[25][i] = automata[26][i] = 
            automata[27][i] = automata[28][i] = automata[29][i] = automata[30][i] = 
            automata[31][i] = automata[32][i] = 4;
        }


        automata[0]['.'] = 2;
        automata[1]['.'] = 2;


        for (int i = 'a'; i <= 'z'; i++)
        {
            automata[0][i] = automata[4][i] = 4;

            automata[11][i] = automata[12][i] = automata[13][i] = automata[14][i] =
            automata[15][i] = automata[16][i] = automata[17][i] = automata[18][i] = 
            automata[19][i] = automata[20][i] = automata[21][i] = automata[22][i] = 
            automata[23][i] = automata[24][i] = automata[25][i] = automata[26][i] = 
            automata[27][i] = automata[28][i] = automata[29][i] = automata[30][i] = 
            automata[31][i] = automata[32][i] = 4;

        }
        for (int i = 'A'; i <= 'Z'; i++)
        {
            automata[0][i] = automata[4][i] = 4;

            automata[11][i] = automata[12][i] = automata[13][i] = automata[14][i] =
            automata[15][i] = automata[16][i] = automata[17][i] = automata[18][i] = 
            automata[19][i] = automata[20][i] = automata[21][i] = automata[22][i] = 
            automata[23][i] = automata[24][i] = automata[25][i] = automata[26][i] = 
            automata[27][i] = automata[28][i] = automata[29][i] = automata[30][i] = 
            automata[31][i] = automata[32][i] = 4;
        }

        // ,, (, ), {, }, \n, \t, ' '
        automata[0]['{'] = 5;
        automata[0]['}'] = 6;
        automata[0]['('] = 7;
        automata[0][')'] = 8;
        automata[0][','] = 9;
        automata[0]['\n'] = automata[0][' '] = automata[0]['\t'] = 10;
        automata[10]['\n'] = automata[10][' '] = automata[10]['\t'] = 10;


        //drz , res , let, ter, par, point, box
        automata[0]['d'] = 11;
        automata[11]['r'] = 12;
        automata[12]['z'] = 13;

        automata[0]['r'] = 14;
        automata[14]['e'] = 15;
        automata[15]['s'] = 16;

        automata[0]['l'] = 17;
        automata[17]['e'] = 18;
        automata[18]['t'] = 19;

        automata[0]['t'] = 20;
        automata[20]['e'] = 21;
        automata[21]['r'] = 22;


        automata[0]['p'] = 23;
        automata[23]['a'] = 24;
        automata[24]['r'] = 25;

        automata[23]['o'] = 26;
        automata[26]['i'] = 27;
        automata[27]['n'] = 28;
        automata[28]['t'] = 29;

        automata[0]['b'] = 30;
        automata[30]['o'] = 31;
        automata[31]['x'] = 32;


        finite[0] = tLexError;
        finite[1] = tInt;
        finite[3] = tReal;
        finite[4] = tString;
        finite[5] = tBegin;
        finite[6] = tEnd;
        finite[7] = tlParant;
        finite[8] = trParant;
        finite[9] = tComma;
        finite[10] = tIgnor;
        finite[13] = tDrzava;
        finite[16] = tRestavracija;
        finite[19] = tLetalisce;
        finite[22] = tTerminal;
        finite[25] = tParkirisce;
        finite[29] = tPoint;
        finite[32] = tBox;
    }
protected:
    int getNextState(int aState, int aChar) const
    {
        if (aChar == -1)
        {
            return noEdge;
        }
        return automata[aState][aChar];
    }
    bool isFiniteState(int aState) const
    {
        return finite[aState] != tLexError;
    }
    int getFiniteStat(int aState) const
    {
        return finite[aState];
    }
protected:
    int peek()
    {
        //cout << "peek: " << input->peek() << endl;
        return input->peek();
    }
    int read()
    {
        int temp = input->get();
        column++;
        if (temp == '\n')
        {
            row++;
            column = 1;
        }
        return temp;
    }
    bool eof()
    {
        return peek() == -1;
    }

    Token nextTokenImp()
    {
        int currentState = startState;
        string lexem = "";
        int startColumn = column;
        int startRow = row;
        int temp = 1;
        do
        {
            int tempState = getNextState(currentState, peek());
            //cout << "test: " << tempState;
            if (tempState != noEdge)
            {
                /*if (tempState == tSeperator)
                {
                    return Token(lexem, startColumn, startRow, finite[temp], eof());
                }*/
                currentState = tempState;
                lexem += (char)read();
                //cout << "lexem: " << lexem<< endl;
                temp = tempState;

                //check if state is finite? and if yes return lexem? or output lexxem and go to next token
            }
            else
            {
                return Token(lexem, startColumn, startRow, temp, eof());
            }

        } while (true);

    }

public:
    const static int tLexError = -1;
    const static int tInt = 1;
    const static int tReal = 3;
    const static int tString = 4;
    const static int tBegin = 5;
    const static int tEnd = 6;
    const static int tlParant = 7;
    const static int trParant = 8;
    const static int tComma = 9;
    const static int tIgnor = 10;
    const static int tDrzava = 13;
    const static int tRestavracija = 16;
    const static int tLetalisce = 19;
    const static int tTerminal = 22;
    const static int tParkirisce = 25;
    const static int tPoint = 29;
    const static int tBox = 32;

    Scanner(istream* aInput)
    {
        row = 1;
        column = 1;
        initAutomata();
        input = aInput;
    }
    Token nextToken()
    {
        return lastToken = nextTokenImp();
    }
    Token currentToken()
    {
        return lastToken;
    }
};





int main(int argc, char* argv[])
{
    if (argc != 3)
    {
        cout << "Stevilo argumentov ni pravilno" << endl;
        return -1;
    }
    else
    {
        map<int, string> m;
        m[1] = "real";
        m[3] = "real";
        m[4] = "variable";
        m[5] = "begin";
        m[6] = "end";
        m[7] = "lparen";
        m[8] = "rparen";
        m[9] = "comma";
        m[10] = "skip";

        m[11] = "variable";
        m[12] = "variable";
        m[13] = "drzava";


        m[14] = "variable";
        m[15] = "variable";
        m[16] = "restavracija";


        m[17] = "variable";
        m[18] = "variable";
        m[19] = "letalisce";


        m[20] = "variable";
        m[21] = "variable";
        m[22] = "terminal";


        m[23] = "variable";
        m[24] = "variable";
        m[25] = "parkirisce";


        m[26] = "variable";
        m[27] = "variable";
        m[28] = "variable";
        m[29] = "point";


        m[30] = "variable";
        m[31] = "variable";
        m[32] = "box";

        

        string inputFile = argv[1];
        string outputFile = argv[2];
        ifstream file(inputFile, std::ifstream::binary);
        if (file)
        {
            Scanner temp(&file);

            Token token = temp.nextToken();


            string outputText = "";

            while (!token.isEof())
            {
                if (m[temp.currentToken().getToken()] != "skip")
                {
                    outputText = outputText + m[temp.currentToken().getToken()] + "(\"" + temp.currentToken().getLexem() + "\") ";
                    //cout << m[temp.currentToken().getToken()] << "(" << temp.currentToken().getLexem() << ") " << endl;

                }


                //cout << temp.currentToken();

                //cout << temp.currentToken() << endl;
                token = temp.nextToken();
            }
            if (m[temp.currentToken().getToken()] != "skip")
            {
                outputText = outputText + m[temp.currentToken().getToken()] + "(\"" + temp.currentToken().getLexem() + "\") ";
                //cout << m[temp.currentToken().getToken()] << "(" << temp.currentToken().getLexem() << ") " << endl;

            }

            ofstream output(outputFile);
            if (output)
            {
                output << outputText;

            output.close();
            }
            file.close();
        }
    }

}
