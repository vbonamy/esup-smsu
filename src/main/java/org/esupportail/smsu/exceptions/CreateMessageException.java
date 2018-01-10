package org.esupportail.smsu.exceptions;

public abstract class CreateMessageException extends Exception {

	private static final long serialVersionUID = 6792087453400066168L;
	
	static public class Wrapper extends CreateMessageException {

		private static final long serialVersionUID = 6792087453400066168L;
	
		private String errorMsg;
		public Exception previousException;

		public Wrapper(String errorMsg, Exception e) {
			this.errorMsg = errorMsg;
			this.previousException = e;
		}

		public String toString() {
			return errorMsg == null ? this.getClass().getName() : errorMsg;
		}

	}

	static public abstract class WebService extends CreateMessageException.Wrapper {
		private static final long serialVersionUID = 1L;

		public WebService(Exception e) { super(null, e); }

	}
	
	@SuppressWarnings("serial")
	static public class WebServiceUnknownApplication extends CreateMessageException.WebService {
		public WebServiceUnknownApplication(Exception e) { super(e); }
	}
	@SuppressWarnings("serial")
	static public class WebServiceInsufficientQuota extends CreateMessageException.WebService {
		public WebServiceInsufficientQuota(Exception e) { super(e); }
	}
	@SuppressWarnings("serial")
	static public class BackOfficeUnreachable extends CreateMessageException.WebService {
		public BackOfficeUnreachable(Exception e) { super(e); }
		public String i18nKey() { return "WS.ERROR.MESSAGE"; }
	}

	static public class GroupQuotaException extends CreateMessageException {

		private static final long serialVersionUID = -6132084495675709441L;

		private String groupName;

		public GroupQuotaException(String groupName) {
			this.groupName = groupName;
		}

		public String toString() {
			return "CreateMessageException.GroupQuotaException(" + groupName + ")";
		}

	}

	static public class UnknownCustomizedTag extends CreateMessageException {

		private static final long serialVersionUID = 6792087453400066168L;
	
		final private String tag;
		
		public UnknownCustomizedTag(final String tag) {
			this.tag = tag;
		}
		
		public String getTag() {
			return tag;
		}
	
		public String toString() {
			return "unknown tag <" + tag + ">";
		}

	}

	static public class CustomizedTagValueNotFound extends CreateMessageException {

		private static final long serialVersionUID = 6792087453400066168L;
	
		final private String tag;
		
		public CustomizedTagValueNotFound(final String tag) {
			this.tag = tag;
		}
		
		public String getTag() {
			return tag;
		}
	
		public String toString() {
			return "tag <" + tag + "> has no value";
		}

	}

	static public class EmptyGroup extends CreateMessageException {
		
		private static final long serialVersionUID = 8476880753507079446L;

		final private String groupName;
		
		public EmptyGroup(final String groupName) {
			this.groupName = groupName;
		}
	
		public String toString() {
			return "group " + groupName + " is empty";
		}
		
	}

}
