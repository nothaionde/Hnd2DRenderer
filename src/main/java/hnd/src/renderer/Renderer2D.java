package hnd.src.renderer;


public class Renderer2D {

	public static void init() {
	}


	public static class Statistics {

		public static int drawCalls = 0;
		public static int quadCount = 0;

		public static int getTotalVertexCount() {
			return quadCount * 4;
		}

		public static int getTotalIndexCount() {
			return quadCount * 6;
		}
	}

}
