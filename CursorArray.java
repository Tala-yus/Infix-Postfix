package application;

public class CursorArray {

	private CANode[] cursorArray;
	private int maxSize = 0;

	public CursorArray(int size) {
		cursorArray = new CANode[size];
		initialization();
	}

	private void initialization() {
		for (int i = 0; i < cursorArray.length - 1; i++)
			cursorArray[i] = new CANode(null, i + 1);
		cursorArray[cursorArray.length - 1] = new CANode(null, 0);

	}

	private int malloc() {
		int p = cursorArray[0].getNext();
		cursorArray[0].setNext(cursorArray[p].getNext());
		return p;
	}

	private void free(int p) {
		cursorArray[p] = new CANode(null, cursorArray[0].getNext());
		cursorArray[0].setNext(p);
	}

	public boolean isNull(int l) {
		return cursorArray[l] == null;
	}

	public boolean isEmpty(int l) {
		return cursorArray[l].getNext() == 0;
	}

	public boolean isLast(int p) {
		return cursorArray[p].getNext() == 0;
	}

	public int createList() {
		int l = malloc();
		if (l == 0)
			System.out.println("Error: Out of space!!!");
		else
			cursorArray[l] = new CANode(null, 0);
		return l;
	}

	public void insertAtHead(String element, int l) {
		if (isNull(l)) // list not created
			return;
		int p = malloc();

		if (p != 0) {
			cursorArray[p] = new CANode(element, cursorArray[l].getNext());
			cursorArray[l].setNext(p);
			maxSize++;
		} else
			System.out.println("Error: Out of space!!!");
	}

	public void traversList(int l) {
		System.out.print("list_" + l + "-->");
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].getNext();
			System.out.print(cursorArray[l] + "-->");
		}
		System.out.println("null");
	}

	public int find(String element, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].getNext();
			if (cursorArray[l].getElement().equals(element))
				return l;
		}
		return -1; // not found
	}

	public int findPrevious(String element, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			if (cursorArray[cursorArray[l].getNext()].getElement().equals(element))
				return l;
			l = cursorArray[l].getNext();
		}
		return -1; // not found
	}

	public CANode delete(String element, int l) {
		int p = findPrevious(element, l);
		CANode result = null;
		if (p != -1) {
			int c = cursorArray[p].getNext();
			CANode temp = cursorArray[c];
			cursorArray[p].setNext(temp.getNext());
			maxSize--;
			result = cursorArray[c];
			free(c);
		}

		return result;
	}

	public void clearList(int l) {
		int t = cursorArray[l].getNext();
		for (int i = 0; i < maxSize; i++) {
			free(t);
			t = cursorArray[t].getNext();
		}

	}

	public CANode deleteHead(int l) {
		return delete(cursorArray[cursorArray[l].getNext()].getElement(), l);
	}

	public CANode[] getCursorArray() {
		return cursorArray;
	}

}
