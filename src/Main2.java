
public class Main2 {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		for(int i=501;i<=544;i++){
//			String s = String.format("<dimen name=\"view_negative_dimen_%s\">%sdp</dimen>",i,-i/3.0f);
//			System.out.println(s);
//		}
		
		for(int i=501;i<=1000;i++){
			String s = String.format("<dimen name=\"margin_%s\">@dimen/view_dimen_%s</dimen>",i,i);
			System.out.println(s);
		}
		String a = "@明天";
		System.out.println(a.length());
		
		
	}

}
