package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.subethamail.smtp.server.Session;

public class EmailTest {
	
	
	private static final String[] TEST_EMAILS = {
			"test1@test.com", "test2@test.com",
			"test3@test.com", "test4@test.com"
	};
	
	private static final String[] TEST_EMAILS2 = {
			"test5@test.com", "test6@test.com",
			"test7@test.com", "test8@test.com",
			"test9@test.com", "test10@test.com",
			"test11@test.com", "test12@test.com"
	};
	
	private static final String[] TEST_CC = {
			"test1@test.com", "test2@test.com",
			"test3@test.com"
	};
	
	private static final String[] TESTNULL = {
		
	};
	
	private EmailConcrete email;
	
	//setup method
	@Before
	public void setUpEmailTest() throws Exception {
		email = new EmailConcrete();
		
	}
	
	//teardown methods
	@After public void tearDownEmailTest() throws Exception {
	//the unit tests are not long running processes
	// so teardown method can be ignored	
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//test addBcc method
	//assert that the size of emails added is the same as expected
	//4 emails expected in this case
	@Test
	public void testAddBcc1() throws Exception {
		
		email.addBcc(TEST_EMAILS);
		
		assertEquals(4, email.getBccAddresses().size());
	}
	
	//test addBcc method
	//assert that the size of emails added is the same as expected
	//8 emails expected in this case
	@Test
	public void testAddBcc2() throws Exception {
	
		email.addBcc(TEST_EMAILS2);
		
		assertEquals(8, email.getBccAddresses().size());
		
	}
	
	//Expect EmailException when there are no emails
	@Test(expected = EmailException.class)
	public void testAddBccEmailException() throws Exception {
	
		email.addBcc(TESTNULL);
		
	}
	

	//test addCc method
    //assert that the size of emails added is the same as expected
	//3 emails expected in this case
	@Test
	public void testAddCc() throws Exception {
				
		email.addCc(TEST_CC);
				
		assertEquals(3, email.getCcAddresses().size());
				
	}

	//Expect EmailException when there are no emails
	@Test(expected = EmailException.class)
	public void testAddCcEmailException() throws Exception {
			
		email.addCc(TESTNULL);
				
	}
	
	//test addHeader method
		//add a header, check to see that header is added
	    //1 header added in this case
		@Test
		public void testAddHeader1() throws Exception {
			
			email.addHeader("name", "value");
			
			assertEquals(1, email.headers.size());
			
		}

		//test addHeader method
		//add a header, check to see that header is added
	    //3 headers added in this case
		@Test
		public void testAddHeader2() throws Exception {
			
			email.addHeader("name1", "value1");
			email.addHeader("name2", "value2");
			email.addHeader("name3", "value3");
			
			assertEquals(3, email.headers.size());
			
		}
		
		//Expect IllegalArgumentException when there is no name in the header
		@Test(expected=IllegalArgumentException.class)
		public void testAddHeaderNullName() throws Exception {
			
			email.addHeader(null, "value");
			
		}
		
		//Expect IllegalArgumentException when there is no value in the header
		@Test(expected=IllegalArgumentException.class)
		public void testAddHeaderNullValue() throws Exception {
			
			email.addHeader("Name", null);
		}
	
		
		//test addReplyTo method
		//add "reply to" information, check to see that information is added
	    //3 added in this case
		@Test
		public void testAddReplyTo() throws Exception {
			
			email.addReplyTo("test1@test.com", "name1");
			email.addReplyTo("test2@test.com", "name2");
			email.addReplyTo("test3@test.com", "name3");
			
			assertEquals(3, email.replyList.size());
		}
		
		//test build mime message
		//test that the "from" email works as expected
		//test that the subject of the message works as expected
		@Test
		public void testBuildMimeMessage() throws Exception {
			
			
			email.setHostName("host1");
			email.setSmtpPort(8080);
			
			email.setFrom("test@test.com");
			email.addTo("toTest@test.com");
			email.setSubject("test mail");
			email.setSslSmtpPort("port1");
			email.setBounceAddress("testBounce@test.com");
			
			final String headerValue = "1212121212";
			email.addHeader("header", headerValue);
			 
			email.buildMimeMessage();
			
			MimeMessage msg = email.getMimeMessage();
			msg.saveChanges();
			
			
			Address[] fromMsg= msg.getFrom();
			String subjectMsg = msg.getSubject();
			
			
			assertEquals(fromMsg, "test@test.com");
			assertEquals(subjectMsg, "test mail");
			
		}
		
		//test build mime message
		//test that the "from" email works as expected when null
		@Test
		public void testBuildMimeMessage2() throws Exception {
			
			
			email.setHostName("host1");
			email.setSmtpPort(8080);
			
			email.addTo("toTest@test.com");
			email.setSubject("test mail");
			email.setSslSmtpPort("port1");
			email.setBounceAddress("testBounce@test.com");
			email.updateContentType("type"); 
			
			email.emailBody = new MimeMultipart();
			
					 
			email.buildMimeMessage();
			
			MimeMessage msg = email.getMimeMessage();
			msg.saveChanges();
			
			
			Address[] fromMsg= msg.getFrom();
			String subjectMsg = msg.getSubject();
			
			
			assertEquals(fromMsg, null);
			
			
		}
		
			//test build mime message
			//test that the "to" email works as expected when null
			@Test
			public void testBuildMimeMessage3() throws Exception {
		
			email.setHostName("host1");
			email.setSmtpPort(8080);
			email.setFrom("test@test.com");
			email.setSubject("test mail");
			email.setSslSmtpPort("port1");
			email.setBounceAddress("testBounce@test.com");
		
					 
			email.buildMimeMessage();
			
			MimeMessage msg = email.getMimeMessage();
			msg.saveChanges();
			
		
			Address[] toMsg= msg.getReplyTo();
			String subjectMsg = msg.getSubject();
			
			
			assertEquals(toMsg, null);
			
		}
		
			//test build mime message with content and email body
		    @Test
			public void testBuildMimeMessage4() throws Exception {
			
			email.setHostName("host1");
			email.setSmtpPort(8080);
			email.addTo("toTest@test.com");
			email.setSubject("test mail");
			email.setSslSmtpPort("port1");
			email.setBounceAddress("testBounce@test.com");
			email.updateContentType("type"); 
			
			email.content = new Object();
			email.emailBody = new MimeMultipart();
				 
			email.buildMimeMessage();
			
			}
		
			//test build mime message with null content
			@Test
			public void testBuildMimeMessage5() throws Exception {
			
			email.setHostName("host1");
			email.setSmtpPort(8080);
			email.addTo("toTest@test.com");
			email.setSubject("test mail");
			email.setSslSmtpPort("port1");
			email.setBounceAddress("testBounce@test.com");
			email.updateContentType("type"); 
			
			
			email.emailBody = new MimeMultipart();
			email.contentType = null;
			
					 
			email.buildMimeMessage();
			
			}

		
		//Expect IllegalStateException in build mime message method
		@Test(expected = IllegalStateException.class)
		public void testBuildMimeMessageException() throws Exception {
		
			javax.mail.Session aSession = null;
			email.message = email.createMimeMessage(aSession);
			
			email.buildMimeMessage();
			
		}
		
		//test that the host name is retrieved correctly
		@Test
		public void testGetHostName() throws Exception {
			
			assertEquals(null, email.getHostName());
			
			String expectHost = "Host1";
			email.hostName = "Host1";
			

			assertEquals(expectHost, email.getHostName());
		}
		
		
		//Expect EmailException in get mail session method
		@Test(expected = EmailException.class)
		public void testGetMailSessionEmailException() throws Exception {
		
			email.getMailSession();
			
		}
		
		
		//testing mail session for the expected properties
		@Test
		public void testGetMailSession() throws Exception {
		
			Properties expectedProperties = new Properties();
			
			expectedProperties.setProperty(email.MAIL_PORT, email.smtpPort);
			
			assertEquals(expectedProperties.setProperty(email.MAIL_PORT, email.smtpPort), email.getMailSession());
			
		}
		
		//test the socket connection timeout
		@Test
		public void testGetSocketConnectionTimeout() throws Exception {
			
			int expectedTimeout = 10;
			email.setSocketConnectionTimeout(10);
			int actualTimeout = email.getSocketConnectionTimeout();
			
			assertTrue(expectedTimeout == actualTimeout);
			
		}

		
		//test the date that the email is sent
		@Test
		public void testGetSentDate() throws Exception {
			
			email.sentDate = null;
			
			assertEquals(new Date(), email.getSentDate());
			
			Date date = new Date();
			email.setSentDate(date);
			
			assertEquals(new Date(email.sentDate.getTime()), email.getSentDate());
			
		}
		
		//test the "from" email 
		@Test
		public void testSetFrom() throws Exception {
			
			email.setFrom("test1@test.com");
			
			assertEquals("test1@test.com", email.fromAddress);
		
			
			
		}
	
	
	
}