package com.mycompany.gvpdriver.server;

import java.net.*;
//import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

public abstract class ServerBase extends Thread  //implements Runnable
{
	private static final Logger log = Logger.getLogger(ServerBase.class);

  /**
   * The server socket: listens for clients who want to connect.
   */
  private ServerSocket serverSocket = null;

  /**
   * The connection listener thread.
   */
  private Thread connectionListener;

  /**
   * The port number
   */
  private int port;

  /**
   * The server timeout while for accepting connections.
   * After timing out, the server will check to see if a command to
   * stop the server has been issued; it not it will resume accepting
   * connections.
   * Set to half a second by default.
   */
  private int timeout = 0; //500;

  /**
   * The maximum queue length; i.e. the maximum number of clients that
   * can be waiting to connect.
   * Set to 10 by default.
   */
  private int backlog = 10;

  /**
   * Indicates if the listening thread is ready to stop.  Set to
   * false by default.
   */
  private boolean readyToStop = false;

  /**
   * Constructs a new instance.
   *
   * @param port the port number on which to listen.
   */
  public ServerBase(int p)  {
    this.port = p;
  }

  /**
   * Begins the thread that waits for new clients.
   * If the server is already in listening mode, this
   * call has no effect.
   *
   * @exception IOException if an I/O error occurs
   * when creating the server socket.
   */
  final public void listen() throws IOException {
	  String fp = "listen: ";
	  
    if (!isListening())   {
      if (serverSocket == null)
      {
        serverSocket = new ServerSocket(getPort(), backlog);
        log.trace(fp + "created new socket");
      }

      //serverSocket.setSoTimeout(timeout);
      readyToStop = false;
      connectionListener = new Thread(this);
      connectionListener.start();
    }
    else {
    	log.info(fp + " is listening, doing nothing");
    }
  }

  /**
   * Causes the server to stop accepting new connections.
   */
  final public void stopListening() {
	  log.info("stopListening: called");
	  readyToStop = true;
  }

  /**
   * Closes the server socket and the connections with all clients.
   * Any exception thrown while closing a client is ignored.
   * If one wishes to catch these exceptions, then clients
   * should be individually closed before calling this method.
   * The method also stops listening if this thread is running.
   * If the server is already closed, this
   * call has no effect.
   *
   * @exception IOException if an I/O error occurs while
   * closing the server socket.
   */
  final synchronized public void close() throws IOException {
	//  String fp = "close: ";
    if (serverSocket == null) {
    	log.info("serverSocket is null, returning");
      return;
    }
      stopListening();
    try  {
      serverSocket.close();
    }
    finally   {
      serverSocket = null;
      serverClosed();
    }
  }

  /**
   * 
   *
   * @param msg   Object The message to be sent
   */
 /* public void sendToAllClients(Object msg)  {
	  String fp = "sendToAllClients: ";

	  log.trace(fp);
  }*/

  /**
   * Returns true if the server is ready to accept new clients.
   *
   * @return true if the server is listening.
   */
  final public boolean isListening()  {
	  return (connectionListener != null);
  }

  /**
   * Returns the port number.
   *
   * @return the port number.
   */
  final public int getPort() {
    return port;
  }

  /**
   * Sets the port number for the next connection.
   * The server must be closed and restarted for the port
   * change to be in effect.
   *
   * @param port the port number.
   */
  final public void setPort(int p)  {
    this.port = p;
  }

  /**
   * Sets the timeout time when accepting connections.
   * The default is half a second. This means that stopping the
   * server may take up to timeout duration to actually stop.
   * The server must be stopped and restarted for the timeout
   * change to be effective.
   *
   * @param timeout the timeout time in ms.
   */
  final public void setTimeout(int tOut) {
    this.timeout = tOut;
  }
  final public int getTimeout() {
	    return this.timeout;
  }

  /**
   * Sets the maximum number of waiting connections accepted by the
   * operating system. The default is 20.
   * The server must be closed and restarted for the backlog
   * change to be in effect.
   *
   * @param backlog the maximum number of connections.
   */
  final public void setBacklog(int bklog) {
    this.backlog = bklog;
  }

  /**
   * Runs the listening thread that allows clients to connect.
   * Not to be called.
   */
  final public void run()  {
	  Socket client = null;
    // call the hook method to notify that the server is starting
    serverStarted();

    try  {
      // Repeatedly waits for a new client connection, accepts it, and
      // starts a new thread to handle data exchange.
      while(!readyToStop) {
    	  //log.info("listening...");
        try  {
        	log.info(this.getName() + " is alive");
          // Wait here for new connection attempts, or a timeout
          client = serverSocket.accept();
          log.info("accepted client, checking");
          
         // InetAddress remote = client.getRemoteSocketAddress();
          //InetAddress local = client.getLocalAddress();
          //InetAddress iAddress = client.getInetAddress();
          log.info("accepted client on " + client.getLocalAddress() + ":" + client.getLocalPort() + " from " + client.getInetAddress() + ":" + client.getPort());
          //synchronized(this)     {
        	launchHandler(client);
          //}
        }
        catch (SocketTimeoutException s) {
           // log.error("SocketTimeoutException: " + s.getMessage());
        } 
        catch (InterruptedIOException exception)    {
          // This will be thrown when a timeout occurs.
          // The server will continue to listen if not ready to stop.
        	//log.error("InterruptedIOException: " + exception.getMessage());
        }
        catch (Exception exception)    {
            // The server will continue to listen if not ready to stop.
          	log.error("Exception: " + exception.getMessage());
          }
        finally{
        	
        }
      }

      // call the hook method to notify that the server has stopped
      serverStopped();
    }
    catch (Exception exception) {
      if (!readyToStop) {
        // Closing the socket must have thrown a SocketException
        listeningException(exception);
      }
      else  {
        serverStopped();
      }
    }
    finally  {
      readyToStop = true;
      connectionListener = null;
    }
  }
  
  protected abstract void launchHandler(Socket s);

  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
 // protected void clientConnected(ConnectionToClient client) {}

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
 // synchronized protected void clientDisconnected(
 //   ConnectionToClient client) {}

  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
 // synchronized protected void clientException(
  //  ConnectionToClient client, Throwable exception) {}

  /**
   * Hook method called when the server stops accepting
   * connections because an exception has been raised.
   * The default implementation does nothing.
   * This method may be overriden by subclasses.
   *
   * @param exception the exception raised.
   */
  protected void listeningException(Throwable exception) {}

  /**
   * Hook method called when the server starts listening for
   * connections.  The default implementation does nothing.
   * The method may be overridden by subclasses.
   */
  protected void serverStarted() {}

  /**
   * Hook method called when the server stops accepting
   * connections.  The default implementation
   * does nothing. This method may be overriden by subclasses.
   */
  protected void serverStopped() {}

  /**
   * Hook method called when the server is clased.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed() {}
}
// End of AbstractServer Class
