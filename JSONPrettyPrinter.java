import java.util.ArrayList;
import java.util.List;

 

/**

*

 * A quick and dirty class for pretty printing a JSON string

*

 * @author rmurali

*

 *

 */

 

public class JSONPrettyPrinter {

 

   

 

    private void printDesiredNumberOfSpaces(StringBuffer buf, int numberOfLevels)

    {

        // print requisite number of indentations

        for (int j = 0; j < numberOfLevels; j++)

        {

            buf.append("  ");

        }

 

    }

 

    private boolean isJSonObject(String jsonString)

    {

        boolean result = false;

        if (!jsonString.isEmpty()) {

            if (jsonString.charAt(0) == '{' && jsonString.charAt(jsonString.length() - 1) == '}') {

                result = true;

            }

        }

        return result;

    }

 

      private boolean isJSonArray(String jsonString) {

            boolean result = false;

            if (!jsonString.isEmpty()) {

                  if (jsonString.charAt(0) == '['

                  && jsonString.charAt(jsonString.length() - 1) == ']') {

 

                        result = true;

                  }

 

            }

            return result;

      }

 

    private boolean isString(String inputString)

    {

 

        boolean result = false;

        if (!inputString.isEmpty()) {

            if (inputString.charAt(0) == '"' && inputString.charAt(inputString.length() - 1) == '"')

            {

                result = true;

            }

        }

        return result;

 

    }

 

    /**

     *

     * A pairing consists of tokens who are either strings or values

     *

     * @param object

     *

     * @return

     */

 

    private ArrayList<String> extractValueTokens(Integer indentationLevel,

            String pairing)

 

    {

 

        ArrayList<String> tokens = new ArrayList<String>();

        int startOfObjectOrArrayCount = 0;

        int startIndex = 0;

        boolean inMiddleOfString = false;

 

        if (pairing.indexOf(":") == -1)

        {

            tokens.add(pairing);

        }

 

        else {

 

            for (int i = 0; i < pairing.length(); i++) {

 

                char c = pairing.charAt(i);

               

                if (c == ':' && startOfObjectOrArrayCount == 0 && !inMiddleOfString)

                {

                    // everything from start index up to but not including : is

                    // either a string or a value

                    // if it's a value it can be an object or an array

 

                    String stringOrValueToken = pairing.substring(startIndex, i);

                    tokens.add(stringOrValueToken);

                    startIndex = i + 1;

 

                }

 

                else if (c == '[' || c == '{')

                {

                    startOfObjectOrArrayCount++;

                }

 

                else if (c == ']' || c == '}')

                {

                    startOfObjectOrArrayCount--;

                }

                else if (c == '"' && !inMiddleOfString)

                {

                    inMiddleOfString = true;

                }

                else if (c == '"' && inMiddleOfString)

                {

                    inMiddleOfString = false;

                }

 

            }

 

            if (startIndex < pairing.length() - 1)

            {

                tokens.add(pairing.substring(startIndex, pairing.length()));            

            }

 

        }

 

        return tokens;

 

    }

 

    /**

     *

     * Retrieves the list of pairings present in a string

     * pairings are delimited by commas

     *

     * @param object

     *

     * @return

     */

 

    private ArrayList<String> getPairs(String object)

 

    {

 

        ArrayList<String> pairings = new ArrayList<String>();

        int startOfObjectOrArrayCount = 0;

        boolean inMiddleOfString = false;

 

        if (object.indexOf(",") == -1)

        {

            pairings.add(object);

        }

 

        else {

 

            int startIndex = 0;

            for (int i = 0; i < object.length(); i++) {

 

                char c = object.charAt(i);

                if (c == ',' && startOfObjectOrArrayCount == 0  && !inMiddleOfString)

                {

                    // everything to start character to the character to the one

                    // before the comma index is a pair

                    pairings.add(object.substring(startIndex, i));

                    startIndex = i + 1;

                }

                else if (c == '[' || c == '{')

                {

                    startOfObjectOrArrayCount++;

                }

                else if (c == ']' || c == '}')

                {

                    startOfObjectOrArrayCount--;

                }

                else if (c == '"' && !inMiddleOfString)

                {

                    inMiddleOfString = true;

                }

                else if (c == '"' && inMiddleOfString)

                {

                    inMiddleOfString = false;

                }

 

            }

 

            // want to add the last pairing as well

            if (startIndex < object.length() - 1)

            {

                pairings.add(object.substring(startIndex, object.length()));

            }

 

        }

 

        return pairings;

 

    }

 

    private void printJsonArray(StringBuffer prettyPrintedJson, String object,

            Integer indentationLevel)

    {

        List<String> values = getPairs(object);

        processStringAndValueTokens(prettyPrintedJson, values, indentationLevel);

    }

 

    private void printJsonObject(StringBuffer prettyPrintedJson, String object,

            Integer indentationLevel)

    {

 

        if (object !=  null)

        {

            object = object.trim();

            if (object.length() != 0)

            {

         

                List<String> pairs = getPairs(object);

                for (String pair : pairs)

                {

 

                    List<String> tokens = extractValueTokens(indentationLevel, pair);

                    processStringAndValueTokens(prettyPrintedJson, tokens, indentationLevel);

                    prettyPrintedJson.append("\n");

 

                }

            }

        }

       

 

    }

 

    private void processStringAndValueTokens(StringBuffer prettyPrintedJson,

            List<String> tokens, Integer indentationLevel)

 

    {

 

        int count = 1;

        for (String token : tokens)

        {

            

            token = token.trim();          

            if (isString(token))

            {

              

                printDesiredNumberOfSpaces(prettyPrintedJson, indentationLevel);

                prettyPrintedJson

                        .append(token.substring(1, token.length() - 1));

            }

 

            else if (isJSonArray(token))

            {

 

                prettyPrintedJson.append("\n");

                printJsonObject(prettyPrintedJson,

                        token.substring(1, token.length() - 1),

                        indentationLevel + 1);

            }

 

            else if (isJSonObject(token))

            {

 

                prettyPrintedJson.append("\n");

                printJsonObject(prettyPrintedJson,

                        token.substring(1, token.length() - 1),

                        indentationLevel + 1);

            }

            else

            {

                // literal value so print as is

                  printDesiredNumberOfSpaces(prettyPrintedJson, indentationLevel);

                prettyPrintedJson.append(token);

            }

 

            //the token list consist os strings followed by values. A colon needs to follow every string

            if (count % 2 == 1)

            {

                prettyPrintedJson.append(":");

            }

 

            count++;

 

        }

 

    }

 

  

    private String prettyPrintJson(String jsonString)

    {

 

        StringBuffer prettyPrintedJson = new StringBuffer();

            if (jsonString != null && !jsonString.isEmpty()) {

                  jsonString = jsonString.trim();

 

                  if (jsonString.charAt(0) == '{'

                              && jsonString.charAt(jsonString.length() - 1) == '}')

 

                  {

 

                        printJsonObject(prettyPrintedJson,

                                    jsonString.substring(1, jsonString.length() - 1), 0);

 

                  }

 

                  else if (jsonString.charAt(0) == '['

                              && jsonString.charAt(jsonString.length() - 1) == ']')

 

                  {

 

                        printJsonArray(prettyPrintedJson,

                                    jsonString.substring(1, jsonString.length() - 1), 0);

 

                  }

            }

 

        return prettyPrintedJson.toString();

 

    }

 

 

   

    

    public static void main(String[] args)

    {

        JSONPrettyPrinter prettyPrinter = new JSONPrettyPrinter();

        if (args.length != 1)

        {

            System.out.println("Usage: java JSONPrettyPrinter <jsonstring>");

        }

        else                                                               

        {

            System.out.println(prettyPrinter.prettyPrintJson(args[0]));

 

        }

       

    }

 

}

 