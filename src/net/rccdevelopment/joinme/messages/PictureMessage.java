package net.rccdevelopment.joinme.messages;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PictureMessage {
	private final static char TRANSPARENT_CHAR = ' ';

	private final Color[] colors = { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0),
			new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0),
			new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85),
			new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85),
			new Color(255, 255, 255), };

	private String[] lines;

	public PictureMessage(BufferedImage image, int height, char imgChar) {
		ChatColor[][] chatColors = toChatColorArray(image, height);
		lines = toImgMessage(chatColors, imgChar);
	}

	public PictureMessage(ChatColor[][] chatColors, char imgChar) {
		lines = toImgMessage(chatColors, imgChar);
	}

	public PictureMessage(String... imgLines) {
		lines = imgLines;
	}

	public PictureMessage appendText(String... text) {
		for (int y = 0; y < lines.length; y++) {
			if (text.length > y) {
				lines[y] += " " + text[y];
			}
		}
		return this;
	}

	public PictureMessage appendCenteredText(String... text) {
		for (int y = 0; y < lines.length; y++) {
			if (text.length > y) {
				int len = 65 - lines[y].length();
				lines[y] = lines[y] + center(text[y], len);
			} else {
				return this;
			}
		}
		return this;
	}

	private ChatColor[][] toChatColorArray(BufferedImage image, int height) {
		double ratio = (double) image.getHeight() / image.getWidth();
		int width = (int) (height / ratio);
		if (width > 10)
			width = 10;
		BufferedImage resized = resizeImage(image, (int) (height / ratio), height);

		ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
		for (int x = 0; x < resized.getWidth(); x++) {
			for (int y = 0; y < resized.getHeight(); y++) {
				int rgb = resized.getRGB(x, y);
				ChatColor closest = getClosestChatColor(new Color(rgb, true));
				chatImg[x][y] = closest;
			}
		}
		return chatImg;
	}

	private String[] toImgMessage(ChatColor[][] colors, char imgchar) {
		String[] lines = new String[colors[0].length];
		for (int y = 0; y < colors[0].length; y++) {
			String line = "";
			for (int x = 0; x < colors.length; x++) {
				ChatColor color = colors[x][y];
				line += (color != null) ? colors[x][y].toString() + imgchar : TRANSPARENT_CHAR;
			}
			lines[y] = line + ChatColor.RESET;
		}
		return lines;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		AffineTransform af = new AffineTransform();
		af.scale(width / (double) originalImage.getWidth(), height / (double) originalImage.getHeight());

		AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return operation.filter(originalImage, null);
	}

	private double getDistance(Color c1, Color c2) {
		double rmean = (c1.getRed() + c2.getRed()) / 2.0;
		double r = c1.getRed() - c2.getRed();
		double g = c1.getGreen() - c2.getGreen();
		int b = c1.getBlue() - c2.getBlue();
		double weightR = 2 + rmean / 256.0;
		double weightG = 4.0;
		double weightB = 2 + (255 - rmean) / 256.0;
		return weightR * r * r + weightG * g * g + weightB * b * b;
	}

	private boolean areIdentical(Color c1, Color c2) {
		return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5
				&& Math.abs(c1.getBlue() - c2.getBlue()) <= 5;

	}

	private ChatColor getClosestChatColor(Color color) {
		if (color.getAlpha() < 128)
			return null;

		int index = 0;
		double best = -1;

		for (int i = 0; i < colors.length; i++) {
			if (areIdentical(colors[i], color)) {
				return ChatColor.values()[i];
			}
		}

		for (int i = 0; i < colors.length; i++) {
			double distance = getDistance(color, colors[i]);
			if (distance < best || best == -1) {
				best = distance;
				index = i;
			}
		}

		return ChatColor.values()[index];
	}

	private String center(String s, int length) {
		if (s.length() > length) {
			return s.substring(0, length);
		} else if (s.length() == length) {
			return s;
		} else {
			int leftPadding = (length - s.length()) / 2;
			StringBuilder leftBuilder = new StringBuilder();
			for (int i = 0; i < leftPadding; i++) {
				leftBuilder.append(" ");
			}
			return leftBuilder.toString() + s;
		}
	}

	public String[] getLines() {
		return lines;
	}

	@SuppressWarnings("deprecation")
	public void sendToPlayer(ProxiedPlayer player) {
		for (String line : lines) {
			player.sendMessage(line);
		}
	}
}
