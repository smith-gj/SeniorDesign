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
            if(phrase.lower() is "poggers"):
                with open("microphone-results.wav", "wb") as f:
                    f.write(audio.get_wav_data())
                    print(audio.get_wav_data())
            print("You have said \n" + phrase)
            print("Audio Recorded Successfully \n ")
        except Exception as e:
            print("Error :  " + str(e))

listen = ListenToVoice
listen()