package com;

import java.io.FileOutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @ClassName: Sendmail
 * @Description: 发送Email
 * @author: 孤傲苍狼
 * @date: 2015-1-12 下午9:42:56
 *
 */
public class SendMail_Attachment {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		prop.setProperty("mail.host", "smtp.163.com");
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = session.getTransport();
		// 3、连上邮件服务器
		ts.connect("smtp.163.com", "****@163.com", "password");
		// 4、创建邮件
		Message message = createAttachMail(session);
		// 5、发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
	}

	/**
	 * @Method: createAttachMail
	 * @Description: 创建一封带附件的邮件
	 * @Anthor:孤傲苍狼
	 *
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static MimeMessage createAttachMail(Session session) throws Exception {
		MimeMessage message = new MimeMessage(session);

		// 设置邮件的基本信息
		// 发件人
        message.setFrom(new InternetAddress("****@163.com"));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("***@qq.com"));
		// 邮件标题
		message.setSubject("邮件发送");

		// 正文
				MimeBodyPart text = new MimeBodyPart();
				text.setContent("xxx这是女的xxxx<br/><img src='cid:aaa.jpg'>", "text/html;charset=UTF-8");

				// 图片
				MimeBodyPart image = new MimeBodyPart();
				image.setDataHandler(new DataHandler(new FileDataSource("src\\1.jpg")));
				image.setContentID("aaa.jpg");
		
		// 创建邮件附件
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource("src\\4.zip"));
		attach.setDataHandler(dh);
		attach.setFileName(MimeUtility.encodeText(dh.getName())); //

		// 描述关系:正文
		MimeMultipart mp1 = new MimeMultipart();
		mp1.addBodyPart(text);
		mp1.addBodyPart(image);
		mp1.setSubType("related");

		// 描述关系:正文和附件
		MimeMultipart mp2 = new MimeMultipart();
		mp2.addBodyPart(attach);

		// 代表正文的bodypart
		MimeBodyPart content = new MimeBodyPart();
		content.setContent(mp1);
		mp2.addBodyPart(content);
		mp2.setSubType("mixed");

		message.setContent(mp2);
		message.saveChanges();

		// 将创建的Email写入到E盘存储
		message.writeTo(new FileOutputStream("E:\\attachMail.eml"));
		// 返回生成的邮件
		return message;
	}
}
