package com.badou.validate.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.base.support.BaseAction;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:18:32
 * @todo 验证码相关类
 */
@RestController
public class YzmCodeAction extends BaseAction {

	private static final int WIDTH = 120;
	private static final int HEIGHT = 30;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:07:06
	 * @todo 构造方法
	 */
	public YzmCodeAction() {
		super();
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:09:09
	 * @todo 生成验证码
	 */
	public void validate() {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		// 1.设置背景颜色
		setBackground(g);
		// 2.设置边框
		setBorder(g);
		// 3.画干扰线
		drawRandomLine(g);
		// 4.生成随机数
		String codes = drawRandomNum((Graphics2D) g);

		//request.getSession().invalidate();
		request.getSession().setAttribute("codes", codes);

		// 5.将图像写给浏览器
		response.setContentType("image/jpeg");

		// 设置浏览器清除缓存
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		try {
			ImageIO.write(image, "png", response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:15:59
	 * @todo 生成随机数
	 * @param
	 * @return String 返回随机数
	 */
	private String drawRandomNum(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("楷体", Font.BOLD, 25));
		String base = "QWERTYUPASDFGHJKZXCVBNM23456789qwertyupasdfghjkzxcvbny";
		StringBuffer buffer = new StringBuffer();
		int x = 20;// 水平开始偏移量
		for (int i = 0; i < 4; i++) {
			int degree = new Random().nextInt() % 30;
			String ch = base.charAt(new Random().nextInt(base.length())) + "";
			buffer.append(ch);
			g.rotate(degree * Math.PI / 180, x, 30);
			g.drawString(ch, x, 25);
			// 必须要有
			g.rotate(-degree * Math.PI / 180, x, 30);
			x += 22;// 水平偏移量
		}
		return buffer.toString().toLowerCase();
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:09:57
	 * @todo 画干扰线
	 */
	private void drawRandomLine(Graphics g) {
		g.setColor(Color.GRAY);
//		for (int i = 0; i < 0; i++) {
//			int x1 = new Random().nextInt(WIDTH);
//			int y1 = new Random().nextInt(HEIGHT);
//			int x2 = new Random().nextInt(WIDTH);
//			int y2 = new Random().nextInt(HEIGHT);
//			g.drawLine(x1, y1, x2, y2);
//		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:10:17
	 * @todo 设置边框
	 */
	private void setBorder(Graphics g) {
		g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:10:26
	 * @todo 设置背景颜色
	 */
	private void setBackground(Graphics2D g) {
		// 设置背景颜色
		g.setColor(getRandColor(new Random(), 200, 250));
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:17:03
	 * @todo 获取随机色值
	 * @param
	 * @return Color 返回对应颜色
	 */
	private Color getRandColor(Random random, int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
