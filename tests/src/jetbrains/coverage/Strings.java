package jetbrains.coverage;

public class Strings {
  //Some japan symbols
  public static final String JP_STRING = new String(new char[] {
    0x0433, 0x0201a, 0x0451, 0x0433, 0x0453, 0x0a0, 0x0433, 0x0201a, 0x0ab, 0x0433, 0x0453, 0x0458, 0x0433, 0x0453, 0x0409, 0x0439, 0x0403, 0x0451, 0x0436, 0x02030, 0x02039, 0x0436, 0x0401, 0x0a9
  }); // "ジムカーナ選手権"

  public static final String LOREN_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque placerat purus ac leo euismod accumsan in in orci. Nullam mollis, eros id auctor bibendum, est risus dignissim eros, quis semper tortor nisl quis arcu. Nulla mattis accumsan velit, ut consectetur arcu lacinia quis. Maecenas pellentesque justo sed sem hendrerit vel hendrerit augue ullamcorper. Proin congue auctor luctus. In hac habitasse platea dictumst. Sed vestibulum volutpat nulla, nec cursus neque bibendum sed. Vestibulum a feugiat metus. Curabitur vitae lectus neque. Phasellus malesuada eleifend dictum. Maecenas at felis sit amet lacus semper posuere at sodales lorem. Praesent urna nisl, ultricies tristique ornare sit amet, pharetra nec turpis. In ut ligula ac risus dictum malesuada eu et quam.";
  public static final String LOCALHOST_IP = "127.0.0.1";

  /**
   * Creates string of required size
   * @param sz size in chars
   * @return string with size about sz
   */
  public static String ofSize(int sz) {
    StringBuilder sb = new StringBuilder(sz + LOREN_IPSUM.length());
    while(sb.length() < sz) sb.append(LOREN_IPSUM);

    return sb.toString();
  }

  public static final String EXOTIC = createExoticString();

  private static String createExoticString() {
    try {
      StringBuilder sb = new StringBuilder();
      for(char i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
        try {
          sb.append(Character.valueOf(i));
        } catch (Throwable t) {
          // NOP
        }
      }
      return sb.toString();
    } catch (Throwable t) {
      return "failed to create exitic string. !@#$%^&*()}{POITREWQASDFGHJKL:\"|?><MNBVCXZ`1234567890-\\/\';][/*-\t\n\r\0\1";
    }
  }
}
