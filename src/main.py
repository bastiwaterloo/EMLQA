import openai
openai.api_key = "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"

prompt = "The average number of overridden methods in your java package is too high. To fix this you should"
model = "text-davinci-003"
response = openai.Completion.create(engine=model, prompt=prompt, max_tokens=50)

generated_text = response.choices[0].text
print(generated_text)


prompt = "The average number of overridden methods in your java package is too high. You should fix this because"
model = "text-davinci-003"
response2 = openai.Completion.create(engine=model, prompt=prompt, max_tokens=50)

generated_text = response2.choices[0].text
print(generated_text)