//
//  ImageChooseViewController.m
//  chengxin
//
//  Created by common on 3/9/16.
//  Copyright © 2016 chengxin. All rights reserved.
//

#import "ImageChooseViewController.h"


@interface ImageChooseViewController ()

@end

@implementation ImageChooseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.chooseView.layer.cornerRadius = 5;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onCamera:(id)sender
{
    UIImagePickerController* imagePicker = [[UIImagePickerController alloc] init];
    [imagePicker setDelegate:self];
    imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:imagePicker animated:YES completion:nil];
    
    //[self dismissViewControllerAnimated:YES completion:nil];
}
-(IBAction)onGallery:(id)sender
{
    UIImagePickerController* imagePicker = [[UIImagePickerController alloc] init];
    [imagePicker setDelegate:self];
    imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    [self presentViewController:imagePicker animated:YES completion:nil];

    
    //[self dismissViewControllerAnimated:YES completion:nil];
    
}
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info
{
    [self dismissViewControllerAnimated:NO completion:nil];
    [self dismissViewControllerAnimated:NO completion:nil];
    [self.delegate chooseViewController:self shownImage:[info objectForKey:UIImagePickerControllerOriginalImage]];
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [self dismissViewControllerAnimated:NO completion:nil];
    [self dismissViewControllerAnimated:NO completion:nil];
}

-(IBAction)onCancel:(UITapGestureRecognizer*)recognizer
{
    [self dismissViewControllerAnimated:NO completion:nil];
}
@end
