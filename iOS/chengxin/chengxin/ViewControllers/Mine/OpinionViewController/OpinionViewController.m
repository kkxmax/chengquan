//
//  OpinionViewController.m
//  chengxin
//
//  Created by common on 4/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "OpinionViewController.h"
#import "Global.h"
#import "OpinionSuccessViewController.h"

@interface OpinionViewController ()

@end

@implementation OpinionViewController
{
    BOOL isTextChanged;
    BOOL isSuccessed;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    isTextChanged = NO;
    isSuccessed = false;
    // Do any additional setup after loading the view from its nib.
    self.lblCounter.text = [NSString stringWithFormat:@"0/100"];
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [IQKeyboardManager sharedManager].enable = NO;
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [IQKeyboardManager sharedManager].enable = YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onSubmit:(id)sender
{
    [self.textView resignFirstResponder];

    if(self.textView.text.length < 10 || isTextChanged == false)
    {
        [appDelegate.window makeToast:@"请输入不少于10个字符"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];

        return;
    }
    if(self.textView.text.length > 100)
    {
        [appDelegate.window makeToast:@"输入最多100个字符"
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];

        return;
    }
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:ACTION_LEAVEOPINION forKey:@"pAct"];
    [dicParams setObject:self.textView.text forKey:@"content"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_LEAVEOPINION Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                isSuccessed = true;
                OpinionSuccessViewController* vc = [[OpinionSuccessViewController alloc] initWithNibName:@"OpinionSuccessViewController" bundle:nil];
                vc.navController = self.navigationController;
                vc.modalPresentationStyle = UIModalPresentationOverFullScreen;
                [self.navigationController presentViewController:vc animated:NO completion:nil];
            }else
            {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }else
        {
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
    }];

}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    if(isSuccessed)
        [self.navigationController popViewControllerAnimated:YES];
}
- (BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    [self.textView setTextColor:[UIColor blackColor]];
    if(!isTextChanged)
        self.textView.text = @"";
    return true;
}
- (void)textViewDidChange:(UITextView *)textView
{
    isTextChanged = YES;
    self.lblCounter.text = [NSString stringWithFormat:@"%lu/100", (unsigned long)self.textView.text.length];
}
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
     //return textView.text.length + (text.length - range.length) <= 100;
    return YES;
}
-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
