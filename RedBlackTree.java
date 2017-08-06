import java.util.Random;
import java.util.Scanner;
/*
 * This program gives the almost increasing subsequence of two array
 * of size 20 containing random integers*/

public class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1;

    private class Node {

        int key = -1;
        int color = 1;
        Node left = nil;
        Node right = nil;
        Node parent = nil;
        int index =0;

        Node(int key) {
            this.key = key;
        } 
    }

    private final Node nil = new Node(-1); 
    private Node root = nil;
    private int counter = 0;
    private Node[] duplicateKey = new Node[20];
    private int inc;
    private int countNodes = 0;

    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(node.key+"\t");
        printTree(node.right);
    }

    private Node findNode(Node findNode, Node node) {
        if (root == nil) {
            return null;
        }
        
        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    private void insert(Node node) {
        Node temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
            	counter++;
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }

    //Takes as argument the newly inserted node
    private void fixTree(Node node) {
        while (node.parent.color == RED) {
        	counter++;
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    void rotateLeft(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateRight(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    //Deletes whole tree
    void deleteTree(){
        root = nil;
    }
    
    //Deletion Code .
    
    //This operation doesn't care about the new Node's connections
    //with previous node's left and right. The caller has to take care
    //of that.
    void transplant(Node target, Node with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.left = with;
          }else
              target.parent.right = with;
          with.parent = target.parent;
    }
    
    boolean delete(Node z){
        if((z = findNode(z, root))==null)return false;
        Node x;
        Node y = z; // temporary reference y
        int y_original_color = y.color;
        
        if(z.left == nil){
            x = z.right;  
            transplant(z, z.right);  
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left); 
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color==BLACK)
            deleteFixup(x);  
        return true;
    }
    
    void deleteFixup(Node x){
        while(x!=root && x.color == BLACK){ 
        	counter++;
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( ) {
        return root.right == nil;
    }
    
    
    Node treeMinimum(Node subTreeRoot){
    	if( isEmpty( ) )
            return nil;
        while(subTreeRoot.left!=nil){
        	counter++;
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
    
    Node treeMaximum(Node subTreeRoot){
    	if( isEmpty( ) )
            return nil;
        while(subTreeRoot.right!=nil){
        	counter++;
            subTreeRoot = subTreeRoot.right;
        }
        return subTreeRoot;
    }
    
    private Node successor(Node x)
    {
    	Node y = nil;
    	if(x == nil)
    		return nil;
    	
    	if(x!=null)
    	{
    		if(x.right != nil)
    		{
    			return treeMinimum(x.right);
    		}
    		y= x.parent;
    		
    		while(y != nil && x==y.right)
    		{
    			counter++;
    			x=y;
    			y=y.parent;
    		}
    	}
    	return y;
    }
    
    private Node predecessor_dup(Node node)
    {
    	Node x = findNode(node,root);
    	Node y =nil;
    	if(x == nil)
    		return nil;
    	
    	if(x!=null)
    	{
    		if(x.left != nil)
    			return treeMaximum(x.left);
    		y= x.parent;
    		while(y != nil && x==y.left)
    		{
    			counter++;
    			x=y;
    			y=y.parent;
    		}
    	}
    	if(y!= nil)
    	{
    		inc=0;
    		checkDuplicate(root);
    		Node temp_check;
    		for(int g=0; g<inc; g++)
    		{
    			counter++;
    			temp_check = duplicateKey[g];
    			if((y.index!=temp_check.index) && (y.key==temp_check.key) && (temp_check.index<y.index))
    				return temp_check;
    		}
    	}
    	return y;
    }
    
    private Node predecessor(Node node)
    {
    	Node x = findNode(node,root);
    	Node y =nil;
    	if(x == nil)
    		return nil;
    	
    	if(x!=null)
    	{
    		if(x.left != nil)
    			return treeMaximum(x.left);
    		y= x.parent;
    		while(y != nil && x==y.left)
    		{
    			counter++;
    			x=y;
    			y=y.parent;
    		}
    	}
    	return y;
    }
 
    private void checkDuplicate(Node node)
    {
    	if (node == nil)
    	{
            return;
        }
        checkDuplicate(node.left);
        duplicateKey[inc]=node;
        inc++;
        checkDuplicate(node.right);
    }
    
    private void MBbinarytree_count(Node node)
    {
    	if(node == nil) return;
    	MBbinarytree_count(node.left);
         countNodes++;
        MBbinarytree_count(node.right);
    }

    
    private void produceSequence(int[] value)
    {
    	int p[]= new int[20];
    	int c=2;
    	Node n1,n2,pred1,pred2,succ1,node;	
    	boolean a1,a2;
    	for(int i=0;i<value.length;i++)
        {
    			 counter++;
            	 node= new Node(value[i]);
            	 node.index=i;
            	 insert(node);
            	 pred1 = predecessor(node);
            	 if(pred1 != nil)
            	 {
            		p[i]=pred1.index;
            	 }
            	 else
            	 {
            		p[i]=i;
            	 }
            	 
            	 n2 = new Node(value[i]+c);
            	 insert(n2);
            	 pred2 = predecessor(n2);
            	 a2=delete(n2);
            	 succ1 = successor(pred2);
            	 
            	 if(succ1 != null)
            		delete(succ1);
            	 printTree(root);
            	 System.out.println();
         }
    	
    	System.out.println("The array of p[i]");
    	for(int z=0;z<p.length;z++)
    	{
    		for(int r=0;r<=z;r++)
    		{
    			System.out.print(p[r]+" ");
    		}
    		System.out.println();
    	}
    	
    	System.out.println("Almost increasing subsequence in reverse order:");
    	Node l=treeMaximum(root);
		int m=l.index;

    	for(int i=value.length-1 ; i >= m+1 ; i--)
    	{
    		counter++;
    		if((value[i] <= value[m]) && (value[m]-c < value[i])) 
    		{
    			System.out.println(value[i]);
    		}
    	}
    	System.out.println(value[m]);
    	
    	int t=m;
    	while( p[t] != t)
    	{
    		counter++;
    		for(int i=t-1; i>= p[t]+1;i--)
    		{
    			counter++;
    			if( ((value[p[t]]-c) < value[i]) && (value[i] <= value[p[t]]) )
    			{
    				System.out.println(value[i]);
    			} 
    		}
    	   System.out.println(value[p[t]]);
    	   t=p[t];
    	}
    }
    
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        RedBlackTree rbt1 = new RedBlackTree();
        
        int[] value = new int[20];
        int[] value1 = new int[20];
    	
        Random rand=new Random();
        Random rand1=new Random();
        int[] c1=new int[1000];
        int[] c2=new int[1000];
        
        for(int h=0;h<1000;h++)
        {
        	for(int z=0;z<20;z++)
        	{
        		value[z]=rand.nextInt(20)+1;
        	}
    	
        	for(int z=0;z<20;z++)
        	{
        		value1[z]=rand1.nextInt(20)+1;
        	}
        	c1[h]=rbt.counter;
        	c2[h]=rbt1.counter;
        }
     
        System.out.println("Output for first sequence");
        System.out.println("The array of z[i]");
        rbt.produceSequence(value);
        System.out.println("Output for Second sequence");
        System.out.println("The array of z[i]");
        rbt1.produceSequence(value1);
        
        
    }
}
