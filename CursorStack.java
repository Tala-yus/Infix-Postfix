package application;

public class CursorStack {

	private CursorArray cursor;
	private int listHead;

	private int stackSize;

	public CursorStack(int size) {
		cursor = new CursorArray(size);
		this.listHead = cursor.createList();
		stackSize = 0;
	}

	public void push(String data) {
		cursor.insertAtHead(data, listHead);
		stackSize++;
	}

	public CANode pop() {
		stackSize--;
		return cursor.deleteHead(listHead);
	}

	public String peek() {
		return cursor.getCursorArray()[cursor.getCursorArray()[listHead].getNext()].getElement();
	}

	public boolean isEmpty() {
		return cursor.isEmpty(listHead);
	}

	public void clear() {
		cursor.clearList(listHead);
		System.out.println("Stack emptied");
	}

	public CursorArray getCursor() {
		return cursor;
	}

	public int getListHead() {
		return listHead;
	}

	public int getStackSize() {
		return stackSize;
	}
}
