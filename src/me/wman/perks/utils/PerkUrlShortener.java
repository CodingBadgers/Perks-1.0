 package me.wman.perks.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class PerkUrlShortener {

	private static final Pattern pattern = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,3})(/\\S*)?$");
	   
	private static String shorten(URI uri) throws Exception {
		// get the tinyurl url
		URL api = new URL("http://tinyurl.com/api-create.php?url=" + uri.toString());
		
		// read the url into the variable
	    BufferedReader in = new BufferedReader(
	    new InputStreamReader(api.openStream()));
	    String url = in.readLine();
	    in.close();
        
		return ChatColor.DARK_AQUA + url + ChatColor.RESET;
	}
	
	private static URI getURI(String input){
        String s = input;

        if (s == null)
        {
            return null;
        }

        Matcher matcher = pattern.matcher(s);

        if (matcher.matches()) {
            try {
                String s1 = matcher.group(0);

                if (matcher.group(1) == null) {
                    s1 = (new StringBuilder()).append("http://").append(s1).toString();
                }

                return new URI(s1);
            }
            catch (URISyntaxException urisyntaxexception) {
                PerkUtils.ErrorConsole("Couldn't create URI from chat");
            }
        }

        return null;
    }
	
	public static boolean isUrl(String input) {
		return getURI(input) != null;
	}
	
	public static String parseMessage(String msg) {
		StringBuilder result = new StringBuilder(msg.length());
		for(StringTokenizer tokenizer = new StringTokenizer(msg, " ", true); tokenizer.hasMoreTokens();) {
			String token = tokenizer.nextToken();
			if (isUrl(token)) {
				URI uri = getURI(token);
				try	{
					String shorten = shorten(uri);
					result.append(shorten);
				} catch(Exception e) {
					result.append(token);
					e.printStackTrace();
				}
			} else {
				result.append(token);
			}
		}
		return result.toString();
	}
}
