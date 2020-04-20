import speech_recognition as sr

def ListenToVoice():
    mic = sr.Recognizer()
    with sr.Microphone() as source:

        mic.adjust_for_ambient_noise(source)

        print("Please say something")

        audio = mic.listen(source)

        print("Recognizing Now .... ")

        try:
            phrase = mic.recognize_google(audio)
            if(phrase.lower() is "listen here new speaker"):
                with open("microphone-results.wav", "wb") as f:
                    f.write(audio.get_wav_data())
            return (phrase)
        except Exception as e:
            return("Error :  " + str(e))