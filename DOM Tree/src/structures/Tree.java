package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root=null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		//if the stack is empty
		if (!sc.hasNextLine())
			return;
		String b = sc.nextLine();
		String nobrac = b.substring(1, b.length()-1);
		//creates the root for HTML tag, no children or siblings
		root = new TagNode(nobrac, null, null);
		//creates a stack and makes the root the first item
		Stack<TagNode> s = new Stack<TagNode>();
		s.push(root);
		
		//traverses through entire html file
		while(sc.hasNextLine()){
			boolean tag=false;
			boolean closetag = false;
			String line = sc.nextLine();
			//checks if it is a tag
			if(line.charAt(0)=='<'){
				tag = true;
				line = line.substring(1, line.length()-1);
				//if it is a tag, then it checks if it is a closing tag
				if(tag== true && line.charAt(0)=='/'){
					closetag = true;
				}
			}
			//if it is a closing tag, pop it
			if(closetag == true){
				s.pop();
			}else{
				TagNode newNode = new TagNode(line, null, null);
				//if the parent tag has no children, make this the first child
				if(s.peek().firstChild==null){
					s.peek().firstChild=newNode;
				}
				//if the parent tag does have children, make this the last child
				else if(s.peek().firstChild!=null){
					TagNode firstChild = s.peek().firstChild;
					while(firstChild.sibling!= null){
						firstChild = firstChild.sibling;
					}
					firstChild.sibling = newNode;
				}
				//if it is a tag, push it so it becomes the new parent tag
				if(tag == true){
					s.push(newNode);
				}
			}
		}
	}
	private boolean punc(char c){
		return ((c=='.')||(c=='?')||(c==':')||(c==';')||(c==',')||(c=='!'));
	}
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		replaceTag2(oldTag, newTag, root);
	}
	private void replaceTag2(String oldTag2, String newTag2, TagNode root) {
		/** COMPLETE THIS METHOD **/
		//base case
		if (root==null){
			return;
		}
		//if the tag equals the oldTag, change the tag to newTag
		else if (root.tag.equals(oldTag2)){
			root.tag = newTag2;
		}
		//do the same for the children and siblings
		replaceTag2(oldTag2, newTag2, root.firstChild);
		replaceTag2(oldTag2, newTag2, root.sibling);
	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		boldRow2(row, root);
	}
	public void boldRow2(int row2, TagNode root){
		//if the tree is empty
		if(root == null)
			return;
		//if the root is table
		if(root.tag.equals("table")){
				int correctRow = 1;
				TagNode rowChild = root.firstChild;
				//makes correctRow equal the specified row that needs to be changed
				while(correctRow!=row2){
					rowChild = rowChild.sibling;
					correctRow++;
				}
				TagNode column = rowChild.firstChild;
				//makes each of the columns of the correct row be bolded
				while(column!= null){
					TagNode bold = new TagNode("b", column.firstChild, null);
					column.firstChild = bold;
					column = column.sibling;
				}
			}
		boldRow2(row2, root.firstChild);
		boldRow2(row2, root.sibling);
	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		removeTag2(tag, root, root);
	}
	public void removeTag2(String tag2, TagNode prev, TagNode root){
		if((root==null)||(prev==null)){
			return;
		}
		else if(root.tag.equals(tag2)){
			//case 1: ol, ul
			if(tag2.equals("ul") || tag2.equals("ol")){
				TagNode child = root.firstChild;
				//makes all of the li children of ol/ul turn into p
				while(child!=null){
					if(child.tag.equals("li")){
						child.tag = "p";
					}
					child=child.sibling;
				}
				removeTag2(tag2, prev, root.firstChild);
				removeTag2(tag2, prev, root.sibling);
			}
			//case 1 and 2
			if(prev.firstChild==root){
				prev.firstChild=root.firstChild;
				TagNode childNode=root.firstChild;
				//gets to the last sibling
				while(childNode.sibling!=null){
					childNode=childNode.sibling;
				}
				childNode.sibling=root.sibling;
				//helps traverse to the children and siblings
				removeTag2(tag2, prev, root.firstChild);
				removeTag2(tag2, prev, root.sibling);
			}
			else if(prev.sibling==root){
				TagNode childNode=root.firstChild;
				//gets to the last sibling
				while(childNode.sibling!=null){
					childNode=childNode.sibling;
				}
				childNode.sibling=root.sibling;
				prev.sibling=root.firstChild;
				//helps traverse to the children and siblings
				removeTag2(tag2, prev, root.firstChild);
				removeTag2(tag2, prev, root.sibling);

			}
			return;

		}
		//helps traverse throughout the entire tree
		prev = root;
		removeTag2(tag2, prev, root.firstChild);
		removeTag2(tag2, prev, root.sibling);
	}


	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addTag2(word, tag, root);
	}
	public void addTag2(String word, String tag, TagNode root){
		if (root==null)
			return;

		String lowTag = root.tag.toLowerCase();
		String lowWord = word.toLowerCase();
		TagNode sib = root.sibling;

		//if the text contains the word that needs to be emphasized
		if (lowTag.contains(lowWord)){
			//if the text is JUST that word
			if (lowTag.equals(lowWord)){
				String old = root.tag;
				root.tag = tag;
				root.firstChild = new TagNode(old, root.firstChild, null);
			}
			//if we need to pick out the word from the text
			else{
				int index = lowTag.indexOf(lowWord);

				//creates a string array that stores the different blocks of the text to single out the target word
				String[] splitup = {root.tag.substring(0, index), root.tag.substring(index, index + word.length()), root.tag.substring(index + word.length(), lowTag.length()), ""};

				//if there is text after the target word and it contains punctuation only in the first index, it adds the punctuation to a new index
				//stores punctuation in the third index of array
				if((splitup[2].length() > 1) && (punc(splitup[2].charAt(0))) && (!punc(splitup[2].charAt(1)))){
					splitup[3] = splitup[2].charAt(0) + "";
					splitup[2] = splitup[2].substring(1);
				}
				else if((splitup[2].length() == 1) && punc(splitup[2].charAt(0))){
					splitup[3] = splitup[2].charAt(0) + "";
					splitup[2] = splitup[2].substring(1);
				}

				//if there is text before the target word, its a space. if there is text after target word, not must be a space (single punc should be placed in array space 3)
				if((splitup[0].length()==0 || splitup[0].substring(splitup[0].length()-1).equals(" ")) && ((splitup[2].length() == 0) || (splitup[2].charAt(0) == ' ' && !punc(splitup[2].charAt(0))))){
					//creating the new node and properly connecting it in the linked list

					TagNode newChild = new TagNode(splitup[1] + splitup[3], null, null);
					if(splitup[0].length()>0){
						root.tag = splitup[0];
						root.sibling = new TagNode(tag, newChild, null);
						if(splitup[0].equals("") || !splitup[2].equals("") || !splitup[3].equals("")){
							if(!splitup[2].equals("")){
								root.sibling.sibling = new TagNode(splitup[2], null, null);
								root.sibling.sibling.sibling = sib;
							}else if(splitup[2].equals("")){
								root.sibling.sibling = sib;
							}
						}
						else{
							root.sibling.sibling = sib;
						}
					}
					else{
						root.sibling = new TagNode(splitup[2], null, null);
						root.tag = tag;
						root.firstChild = newChild;
					}
				}
			}
			if (sib!=null){
				addTag2(word, tag, sib);
			}
		}
		else{
			//used to help traverse throughout the whole tree
			addTag2(word, tag, root.firstChild);
			addTag2(word, tag, root.sibling);
		}
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}

	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}

	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}

