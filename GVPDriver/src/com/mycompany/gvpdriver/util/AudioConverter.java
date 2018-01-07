package com.mycompany.gvpdriver.util;

/** @copyright 2005-2009 mycompany . */

//import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

import org.apache.log4j.Logger;

/**
 * @file			AudioConverter.java
 * 
 * @description 	This class is responsible for audio files' format 
 *                  validation / conversion
 * 
 * @author   		Tatiana Stepourska
 *
 * @date  			Nov 17, 2005
 */
public class AudioConverter extends FormatConversionProvider 
{
	private static final Logger logger = Logger.getLogger(AudioConverter.class);
	
	/** List of  formats accepted by VS */
	protected AudioFormat[]      format = null;
	
	/** Default constructor */
	public AudioConverter()
	{
		this.format = new AudioFormat[] {
				new  AudioFormat(AudioFormat.Encoding.ULAW,  //encoding, 
								8000.0f,                  //sampleRate,
								8,                        //sampleSizeInBits, 
								1,                        //channels, 
								1,                        //frameSize, 
								8000.0f,                  //frameRate, 
								false),                     //bigEndian
				
				new  AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED,  //encoding, 
								8000.0f,                  //sampleRate,
								8,                        //sampleSizeInBits, 
								1,                        //channels, 
								1,                        //frameSize, 
								8000.0f,                  //frameRate, 
								false)                    //bigEndian				
		};
	}	//end of constructor
	
	/**
	 * Mode is application type - Peri or VG
	 * @param mode
	 */
	public AudioConverter(int mode) {
		switch (mode) {
		//case  VS_MODE_PERI_LOOSE:
		//	this.format = new AudioFormat[] {

			/*new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, // encoding,
					8000.0f, // sampleRate,
					16, // sampleSizeInBits,
					1, // channels,
					1, // frameSize,
					8000.0f, // frameRate,
					false) // bigEndian
			};*/
			
		//	new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // encoding,
		//			8000.0f, // sampleRate,
		//			16, // sampleSizeInBits,
		//			1, // channels,
		//			2, // frameSize,
		//			8000.0f, // frameRate,
		//			false) // bigEndian
		//	};

		//	break;
		//case  VS_MODE_PERI_RESTRICTED:
		//	this.format = new AudioFormat[] {

			/*new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, // encoding,
					8000.0f, // sampleRate,
					16, // sampleSizeInBits,
					1, // channels,
					1, // frameSize,
					8000.0f, // frameRate,
					false) // bigEndian
			};*/
			
		//	new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // encoding,
		//			8000.0f, // sampleRate,
		//			16, // sampleSizeInBits,
		//			1, // channels,
		//			2, // frameSize,
		//			8000.0f, // frameRate,
		//			false) // bigEndian
		//	};

		//	break;
			
		// synrevoice will use this one, please do not delete it.
	/*	case  APP_TYPE_PERI: 
			this.format = new AudioFormat[] {
			
			new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, // encoding,
						16000.0f, // sampleRate,
						8, // sampleSizeInBits,
						1, // channels,
						1, // frameSize,
						16000.0f, // frameRate,
						false) // bigEndian
			,
			
			new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // encoding,
					8000.0f, // sampleRate,
					16, // sampleSizeInBits,
					1, // channels,
					2, // frameSize,
					8000.0f, // frameRate,
					false) // bigEndian
			};

			break;*/
		default:
			this.format = new AudioFormat[] {

				new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, // encoding,
						8000.0f, // sampleRate,
						16, // sampleSizeInBits,
						1, // channels,
						1, // frameSize,
						8000.0f, // frameRate,
						false) // bigEndian
				,
				
				new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // encoding,
						8000.0f, // sampleRate,
						16, // sampleSizeInBits,
						1, // channels,
						2, // frameSize,
						8000.0f, // frameRate,
						false) // bigEndian
				};
		}
	} // end of constructor
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getSourceEncodings()
	 */
	public Encoding[] getSourceEncodings() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getTargetEncodings()
	 */
	public Encoding[] getTargetEncodings() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getTargetEncodings(javax.sound.sampled.AudioFormat)
	 */
	public Encoding[] getTargetEncodings(AudioFormat arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getTargetFormats(javax.sound.sampled.AudioFormat.Encoding, javax.sound.sampled.AudioFormat)
	 */
	public AudioFormat[] getTargetFormats(Encoding arg0, AudioFormat arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getAudioInputStream(javax.sound.sampled.AudioFormat.Encoding, javax.sound.sampled.AudioInputStream)
	 */
	public AudioInputStream getAudioInputStream(Encoding arg0,
			AudioInputStream arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sound.sampled.spi.FormatConversionProvider#getAudioInputStream(javax.sound.sampled.AudioFormat, javax.sound.sampled.AudioInputStream)
	 */
	public AudioInputStream getAudioInputStream(AudioFormat arg0,
			AudioInputStream arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Validates the format of the loaded audio file against the 
	 * standard format accepted by the Virtual Studio
	 * 
	 * @param f
	 * @param standard
	 * 
	 * @return boolean
	 * 
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public boolean validate(File f) throws IOException, UnsupportedAudioFileException
	{
		AudioFileFormat aff = AudioSystem.getAudioFileFormat(f);
		AudioFormat af      = aff.getFormat();		
		
		///////////////////////////////////
		logger.info("Channels        : " + af.getChannels());
		logger.info("FrameRate       : " + af.getFrameRate());
		logger.info("FrameSize       : " + af.getFrameSize());
		logger.info("SampleRate      : " + af.getSampleRate());
		logger.info("SampleSizeInBits: " + af.getSampleSizeInBits());
		logger.info("Encoding        : " + af.getEncoding());
		logger.info("IsBigEndian     : " + af.isBigEndian());		
		///////////////////////////////////
		
		///////////////////////////////////
		// to try
		//af.
		// end of to try
		////////////////////////////////////
		
		for(int i=0;i<this.format.length;i++)
		{
			if(af.matches(this.format[i]))
				return true;
		}
		return false;
	}
	
	/**
	 * Validates the format of the loaded audio file against the 
	 * standard format accepted by the Virtual Studio
	 * 
	 * @param f
	 * @param standard
	 * 
	 * @return boolean
	 * 
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public boolean validate(AudioInputStream in) throws IOException, UnsupportedAudioFileException
	{
		logger.info("validate: started");
		
		AudioFileFormat aff = AudioSystem.getAudioFileFormat(in);
		AudioFormat af      = aff.getFormat();		
		
		///////////////////////////////////
		logger.info("Channels        : " + af.getChannels());
		logger.info("FrameRate       : " + af.getFrameRate());
		logger.info("FrameSize       : " + af.getFrameSize());
		logger.info("SampleRate      : " + af.getSampleRate());
		logger.info("SampleSizeInBits: " + af.getSampleSizeInBits());
		logger.info("Encoding        : " + af.getEncoding());
		logger.info("IsBigEndian     : " + af.isBigEndian());		
		///////////////////////////////////
		
		for(int i=0;i<this.format.length;i++)
		{
			if(af.matches(this.format[i]))
				return true;
		}
		return false;
	}
	
	/** Determes the File Format of a Sampled Audio File */
	public boolean determineFormat(File f)
	{
		AudioFileFormat ft = null;
		
		try 
		{
			// From file
		    //format = AudioSystem.getAudioFileFormat(new File("audiofile"));
			ft = AudioSystem.getAudioFileFormat(f);
			
		    // From URL
		    //format = AudioSystem.getAudioFileFormat(new URL("http://hostname/audiofile"));
		    
		    if (ft.getType() == AudioFileFormat.Type.WAVE) 
		    {
		    	
		    	return true;
		    }
		    else if (ft.getType() == AudioFileFormat.Type.AIFC) 
		    {
		    	throw new UnsupportedAudioFileException("AIFC format is not supported");
		    } 
		    else if (ft.getType() == AudioFileFormat.Type.AIFF) 
		    {
		    	throw new UnsupportedAudioFileException("AIFF format is not supported");
		    } 
		    else if (ft.getType() == AudioFileFormat.Type.AU) 
		    {
		    	throw new UnsupportedAudioFileException("AU format is not supported");
		    } 
		    else
		    {
		    	throw new UnsupportedAudioFileException("Unknown format is not supported");
		    }
		} 
		catch (MalformedURLException e) 
		{
		    return false;
		} 
		catch (IOException e) 
		{
			return false;
		} 
		catch (UnsupportedAudioFileException e) 
		{
		        // File format is not supported.
			return false;
		}
		catch (Exception e) 
		{
			return false;
		}

	}
		
	/** Determines the Encoding of a Sampled Audio File	 */
	public boolean determineEncoding()
	{
	 try 
	 {
	        // From file
	        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("audiofile"));
	    
	        // From URL
	        stream = AudioSystem.getAudioInputStream(new URL("http://hostname/audiofile"));
	    
	        AudioFormat ft = stream.getFormat();
	        if (ft.getEncoding() == AudioFormat.Encoding.ULAW) 
	        {
	        	 return false;
	        } 
	        else if (ft.getEncoding() == AudioFormat.Encoding.ALAW) 
	        {
	        	 return false;
	        }
	        else if(ft.getEncoding() == AudioFormat.Encoding.PCM_SIGNED)
	        {
	        	 return true;
	        }
	        else if(ft.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED)
	        {
	        	//format.
	        	 return true;
	        }
	 } 
	 catch (MalformedURLException e) 
	 {
	    return false;
	 } 
	 catch (IOException e) 
	 {
		 return false;
	 } 
	 catch (UnsupportedAudioFileException e) 
	 {
	        // Audio format is not supported.
		 return false;
	 }
	 	return false;
	}
	
	/** Determines the duration of a sampled audio file	 */
	public void determineDuration() throws Exception
	{
		//		 From file
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("audiofile"));
        // At present, ALAW and ULAW encodings must be converted
        // to PCM_SIGNED before it can be played
        AudioFormat ft = stream.getFormat();
		 // Create the clip
        DataLine.Info info = new 
        DataLine.Info(Clip.class, 
        			stream.getFormat(), 
        			((int)stream.getFrameLength()*ft.getFrameSize()));
        
        Clip clip = (Clip)AudioSystem.getLine(info);
    
		double durationInSecs = clip.getBufferSize() / (clip.getFormat().getFrameSize() * clip.getFormat().getFrameRate());
		logger.info("durationInSecs:" + durationInSecs);
	}
	
	/**
	 * Plays audio file. Supported audio file formats: aif, au, and wav. 
	 *
	 */
	public void playAudio()
	{
		
		   try 
		   {
		        // From file
		        AudioInputStream stream = AudioSystem.getAudioInputStream(new File("audiofile"));
		    
		        // From URL
		        stream = AudioSystem.getAudioInputStream(new URL("http://hostname/audiofile"));
		    
		        // At present, ALAW and ULAW encodings must be converted
		        // to PCM_SIGNED before it can be played
		        AudioFormat ft = stream.getFormat();
		        if (ft.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
		        	ft = new AudioFormat(
		                    AudioFormat.Encoding.PCM_SIGNED,
		                    ft.getSampleRate(),
		                    ft.getSampleSizeInBits()*2,
		                    ft.getChannels(),
		                    ft.getFrameSize()*2,
		                    ft.getFrameRate(),
		                    true);        // big endian
		            stream = AudioSystem.getAudioInputStream(ft, stream);
		        }
		    
		        // Create the clip
		        DataLine.Info info = new 
		        DataLine.Info(Clip.class, 
		        			stream.getFormat(), 
		        			((int)stream.getFrameLength()*ft.getFrameSize()));
		        
		        Clip clip = (Clip)AudioSystem.getLine(info);
		    
		        // This method does not return until the audio file is completely loaded
		        clip.open(stream);
		    
		        // Start playing
		        clip.start();
		    } catch (MalformedURLException e)         {
		    } catch (IOException e)                   {
		    } catch (LineUnavailableException e)      {
		    } catch (UnsupportedAudioFileException e) {
		    }
	}

}