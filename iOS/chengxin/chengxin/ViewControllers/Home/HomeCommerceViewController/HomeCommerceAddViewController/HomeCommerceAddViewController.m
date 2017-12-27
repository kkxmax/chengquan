//
//  HomeCommerceAddViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceAddViewController.h"
#import "HomeChoiceBusinessViewController.h"
#import "Global.h"
#import "SearchProductsViewController.h"
#import "SDWebImageManager.h"

@interface HomeCommerceAddViewController ()<HomeChoiceBusinessViewDelegate>
{
    HomeChoiceBusinessViewController *choiceCategoryVC;
    NSDictionary *selectedCategoryDic;
    ImageChooseViewController *logoPicker;
    NSString* pleixingID;
}
@end

@implementation HomeCommerceAddViewController
@synthesize viewPicture, heightPicView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
   
    NSMutableArray *aryPic = [[NSMutableArray alloc] init];
    CGRect rect = viewPicture.frame;
    viewPicture.delegate = self;
    viewPicture.maximumPictureCount = 5;
    [viewPicture setFrame:rect :aryPic];
    heightPicView.constant = viewPicture.frame.size.height;
    if([[CommonData sharedInstance].lastClick isEqualToString:@"SearchProductsViewController"])
    {
        self.shareButton.hidden = YES;
    }
    if(self.product != nil)
    {
        self.nameTextField.text = self.product[@"name"];
        self.isMainTrueButton.selected = [self.product[@"isMain"] boolValue];
        self.isMainFalseButton.selected = ![self.product[@"isMain"] boolValue];
        self.priceTextField.text = [self.product[@"price"] stringValue];
        self.categoryLabel.text = self.product[@"pleixingName"];
        pleixingID = [self.product[@"pleixingId"] stringValue];
        self.commentTextView.text = self.product[@"comment"];
        [self.commentTextView scrollRectToVisible:CGRectMake(0, 0, 5, 5) animated:NO];
        self.weburlTextField.text = self.product[@"weburl"];
        self.addressTextField.text = self.product[@"saleAddr"];
        NSArray* aryPictures = self.product[@"imgPaths"];
        for(int i = 0; i < aryPictures.count; i++)
        {
            NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, aryPictures[i]]];
            [SDWebImageManager.sharedManager downloadImageWithURL:url options:0 progress:nil completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, BOOL finished, NSURL *imageURL) {
                if(image != nil)
                    [viewPicture addPicture:image];
            }];
        }
        
    }
    self.productCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", self.commentTextView.text.length];
   
    /*
    [dicParams setObject:self.nameTextField.text forKey:@"name"];
    [dicParams setObject:self.isMainTrueButton.selected? @"1" : @"0" forKey:@"isMain"];
    [dicParams setObject:self.priceTextField.text forKey:@"price"];
    [dicParams setObject:selectedCategoryDic[@"id"] forKey:@"pleixingId"];
    [dicParams setObject:self.commentTextView.text forKey:@"comment"];
    [dicParams setObject:self.weburlTextField.text forKey:@"weburl"];
    [dicParams setObject:self.addressTextField.text forKey:@"saleAddr"];
     */
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    //[self.commentTextView scrollRangeToVisible:NSMakeRange(0, 1)];
    
}
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self.commentTextView scrollRectToVisible:CGRectMake(0, 0, 5, 5) animated:NO];
}

- (IBAction)onChoiceKindAction:(id)sender {
    [self.view endEditing:YES];
    choiceCategoryVC = [[HomeChoiceBusinessViewController alloc] initWithNibName:@"HomeChoiceBusinessViewController" bundle:nil];
    choiceCategoryVC.isCancelButton = YES;
    choiceCategoryVC.delegate = self;
    choiceCategoryVC.mChoice = CHOICE_ADD_COMMERCE;
    choiceCategoryVC.isSingleSelectionMode = NO;
    choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, 64.f, self.view.frame.size.width - 44, self.view.frame.size.height - 64.f);
    choiceCategoryVC.view.hidden = NO;
    [self.view addSubview:choiceCategoryVC.view];
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width - choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        self.transView.hidden = NO;
    }];
}

- (IBAction)onAdd:(id)sender {
    [self.view endEditing:YES];
    if(self.nameTextField.text.length == 0) {
        [appDelegate.window makeToast:@"请输入产品名称"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    if(self.priceTextField.text.length == 0) {
        [appDelegate.window makeToast:@"请输入产品价格"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    if(self.priceTextField.text.length == 0) {
        [appDelegate.window makeToast:@"请输入产品价格"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    if(selectedCategoryDic) {
        if(selectedCategoryDic[@"id"] == nil) {
            [appDelegate.window makeToast:@"请选择产品分类"
                                 duration:3.0
                                 position:CSToastPositionCenter
                                    style:nil];
            return;
        }
    }
    else if(pleixingID) {
        if(pleixingID.length == 0) {
            [appDelegate.window makeToast:@"请选择产品分类"
                                 duration:3.0
                                 position:CSToastPositionCenter
                                    style:nil];
            return;
        }
    }
    if(self.commentTextView.text.length == 0) {
        [appDelegate.window makeToast:@"请输入产品介绍"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    if(self.weburlTextField.text.length == 0 && self.addressTextField.text.length == 0) {
        [appDelegate.window makeToast:@"产品网址和实体店地址至少应该填写一个"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    if(viewPicture.aryPicture.count == 0) {
        [appDelegate.window makeToast:@"请选择产品图片"
                             duration:3.0
                             position:CSToastPositionCenter
                                style:nil];
        return;
    }
    [GeneralUtil showProgress];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"addProduct" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    if(self.product != nil)
    {
        [dicParams setObject:self.product[@"id"] forKey:@"productId"];
    }
    [dicParams setObject:self.nameTextField.text forKey:@"name"];
    [dicParams setObject:self.isMainTrueButton.selected? @"1" : @"0" forKey:@"isMain"];
    [dicParams setObject:self.priceTextField.text forKey:@"price"];
    if(selectedCategoryDic)
        [dicParams setObject:selectedCategoryDic[@"id"] forKey:@"pleixingId"];
    else if(pleixingID)
        [dicParams setObject:pleixingID forKey:@"pleixingId"];

    [dicParams setObject:self.commentTextView.text forKey:@"comment"];
    [dicParams setObject:self.weburlTextField.text forKey:@"weburl"];
    [dicParams setObject:self.addressTextField.text forKey:@"saleAddr"];

    NSMutableDictionary *imageDictionary = [NSMutableDictionary dictionary];
    NSMutableArray *imageDataArray = [NSMutableArray array];
    for (int i = 0; i < viewPicture.aryPicture.count; i++) {
        NSData* image = UIImagePNGRepresentation(viewPicture.aryPicture[i]);
        [imageDataArray addObject:image];
    }
    
    [imageDictionary setObject:imageDataArray forKey:@"images"];

    [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_ADDPRODUCT Parameters:dicParams UploadImages:imageDictionary :^(NSObject *resObj) {
        
        [GeneralUtil hideProgress];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                if(self.product == nil)
                    [CommonData sharedInstance].isPublished = true;

                if([[CommonData sharedInstance].lastClick isEqualToString:@"MainViewController"])
                {
                    SearchProductsViewController *searchProductsVC = [[SearchProductsViewController alloc] initWithNibName:@"SearchProductsViewController" bundle:nil];
                    searchProductsVC.selected_tab = em_Product;
                    UINavigationController* navController = self.navigationController;
                    [self.navigationController popViewControllerAnimated:NO];
                    [navController pushViewController:searchProductsVC animated:YES];
                }else
                {
                    [self.navigationController popViewControllerAnimated:YES];
                }
                
            }else{
                [appDelegate.window makeToast:dicRes[@"msg"]
                                                 duration:3.0
                                                 position:CSToastPositionCenter
                                                    style:nil];
            }
        }else{
            [appDelegate.window makeToast:@"服务器繁忙，请稍后重试"
                                             duration:3.0
                                             position:CSToastPositionCenter
                                                style:nil];
        }
    }];
}

- (IBAction)onBackAction:(id)sender {
    [self.view endEditing:YES];
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onMainAction:(id)sender {
    [self.view endEditing:YES];
    UIButton *button = (UIButton *)sender;
    if(self.isMainTrueButton == button) {
        if(self.isMainTrueButton.selected)
            return;
        self.isMainTrueButton.selected = YES;
        self.isMainFalseButton.selected = NO;
    }else{
        if(self.isMainFalseButton.selected)
            return;
        self.isMainFalseButton.selected = YES;
        self.isMainTrueButton.selected = NO;
    }
}

#pragma mark - ChoiceCategoryViewDelegate
- (void)hideChoiceBussinessView:(NSDictionary *)categoryDic {
    self.transView.hidden = YES;
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        choiceCategoryVC.view.hidden = YES;
        [choiceCategoryVC.view removeFromSuperview];
    }];
    if(categoryDic) {
        selectedCategoryDic = categoryDic;
        self.categoryLabel.text = categoryDic[@"title"];
    }
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    NSInteger textLength = [self.commentTextView.text length];
    if(textLength <= 300) {
        self.productCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", (int)textLength];
    }
}
- (BOOL)textView:(UITextView *)iTextView shouldChangeTextInRange:(NSRange)iRange replacementText:(NSString *)iText {
    if (iRange.location < 300) {
        return YES;
    }else
        return NO;
    //iTextView.text = [iText substringToIndex:300];
    return NO;
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    if(textField == self.nameTextField)
    {
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }
    /*else if(textField == self.passwordInput)
    {
        //if([string isEqualToString:@" "])
        //    return NO;
        if(range.location >= 20)
            return NO;
        else
            return YES;
    }
    */
    return YES;
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

-(void) changedPictureView:(NSMutableArray *)aryPicture :(int)height {
    heightPicView.constant = height;
}

- (IBAction)handleTapCategoryView:(UITapGestureRecognizer *)recognizer {
    self.transView.hidden = YES;
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        choiceCategoryVC.view.hidden = YES;
        [choiceCategoryVC.view removeFromSuperview];
    }];
}

@end
