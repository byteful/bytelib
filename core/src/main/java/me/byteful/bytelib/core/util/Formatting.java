package me.byteful.bytelib.core.util;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class Formatting {
  private static final Set<Character> COLOR_CHARS = "4c6e2ab319d5f780rlonmk".chars().mapToObj(i -> (char) i).collect(Collectors.toSet());

  // https://github.com/Redempt/RedCommands/blob/1.5.7/src/redempt/redlib/misc/FormatUtils.java
  // Thank you, Redempt
  public static String color(String input) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (i + 1 >= input.length()) {
        builder.append(c);
        continue;
      }
      char n = input.charAt(i + 1);
      if (c == '\\' && (n == '&' || n == '\\')) {
        i++;
        builder.append(n);
        continue;
      }
      if (c != '&') {
        builder.append(c);
        continue;
      }
      if (COLOR_CHARS.contains(n)) {
        builder.append(ChatColor.COLOR_CHAR);
        continue;
      }
      if (n == '#' && i + 7 <= input.length()) {
        String hexCode = input.substring(i + 2, i + 8).toUpperCase(Locale.ROOT);
        if (hexCode.chars().allMatch(ch -> (ch <= '9' && ch >= '0') || (ch <= 'F' && ch >= 'A'))) {
          hexCode = Arrays.stream(hexCode.split("")).map(s -> ChatColor.COLOR_CHAR + s).collect(Collectors.joining());
          builder.append(ChatColor.COLOR_CHAR).append("x").append(hexCode);
          i += 7;
          continue;
        }
      }
      builder.append(c);
    }
    return builder.toString();
  }

  public static List<String> color(List<String> input) {
    return input.stream().map(Formatting::color).collect(Collectors.toList());
  }
}
